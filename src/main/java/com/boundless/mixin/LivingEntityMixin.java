package com.boundless.mixin;

import com.boundless.BoundlessAPI;
import com.boundless.ability.BlockLogic;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.CombatUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);
    @Shadow public abstract @Nullable StatusEffectInstance getStatusEffect(RegistryEntry<StatusEffect> effect);
    @Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);
    @Unique LivingEntity livingEntity = (LivingEntity) (Object)this;
    @Unique boolean isBlockingDamage = false;

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    protected void boundless$modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (source.isOf(DamageTypeRegistry.BYPASS_DEFENCE) || !(livingEntity instanceof PlayerEntity player)) {
            cir.setReturnValue(amount);
            return;
        }

        float damageResistance = (float) (this.getAttributeValue(AttributeRegistry.DAMAGE_RESISTANCE) - 1);
        float resistanceReduction = 0.0f;

        if (this.hasStatusEffect(StatusEffectRegistry.BONE_BREAK_EFFECT)) {
            resistanceReduction = (Objects.requireNonNull(this.getStatusEffect(StatusEffectRegistry.BONE_BREAK_EFFECT)).getAmplifier() + 1) * 0.1f;
            resistanceReduction = Math.clamp(resistanceReduction, 0.0f, damageResistance);
        }

        float finalDamage = amount - (amount * (damageResistance - resistanceReduction));
        cir.setReturnValue(finalDamage);
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void boundless$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(AttributeRegistry.DAMAGE_RESISTANCE);
        cir.getReturnValue().add(AttributeRegistry.TOP_SPEED_MULTIPLIER);
        cir.getReturnValue().add(AttributeRegistry.TIME_UNTIL_MAX_SPEED);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void boundless$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (isBlockingDamage) return;
        if (!(livingEntity instanceof PlayerEntity player)) return;

        float postBlockDamage = getPostBlockDamage(source, player, amount);

        if (postBlockDamage <= 0.0) {
            cir.setReturnValue(false);
            CombatUtils.playImpactVisual(player, livingEntity, BoundlessAPI.identifier("melee_impact"), 0.5f);
            return;
        }

        // todo: I hate this but it will do for now lol
        if (postBlockDamage < amount) {
            isBlockingDamage = true;
            ((LivingEntity)(Object)this).damage(source, postBlockDamage);
            isBlockingDamage = false;
            cir.setReturnValue(false);
        }
    }

    @Unique
    private float getPostBlockDamage(DamageSource source, PlayerEntity player, float initialAmount) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        float blockHP = stack.getOrDefault(DataComponentRegistry.BLOCK_HP, 100f);

        if (blockHP > 0.0 && BlockLogic.isBlocking(player) && BlockLogic.shouldBlockDamage(source, player)) {
            if (blockHP > initialAmount) {
                stack.set(DataComponentRegistry.BLOCK_HP, blockHP - initialAmount);
                return 0.0f;
            } else {
                float excessDamage = initialAmount - blockHP;
                stack.set(DataComponentRegistry.BLOCK_HP, Math.max(0.0f, blockHP - initialAmount));
                return excessDamage;
            }
        }
        return initialAmount;
    }
}