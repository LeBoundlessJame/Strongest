package com.boundless.mixin;

import com.boundless.ability.BlockLogic;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.StatusEffectRegistry;
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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow
    public abstract @Nullable StatusEffectInstance getStatusEffect(RegistryEntry<StatusEffect> effect);

    @Shadow
    public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    protected void boundless$modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (source.isOf(DamageTypeRegistry.BYPASS_DEFENCE) || (!((LivingEntity)(Object)this instanceof PlayerEntity player))) {
            cir.setReturnValue(amount);
            return;
        }

        float postBlockDamage = getPostBlockDamage(source, player, amount);
        if (postBlockDamage <= 0.0f) cir.setReturnValue(0.0f);

        float damageResistance = (float) (this.getAttributeValue(AttributeRegistry.DAMAGE_RESISTANCE) - 1);
        float resistanceReduction = 0.0f;

        if (this.hasStatusEffect(StatusEffectRegistry.BONE_BREAK_EFFECT)) {
            resistanceReduction = (Objects.requireNonNull(this.getStatusEffect(StatusEffectRegistry.BONE_BREAK_EFFECT)).getAmplifier() + 1) * 0.1f;
            resistanceReduction = Math.clamp(resistanceReduction, 0.0f, damageResistance);
        }

        cir.setReturnValue (amount - (amount * (damageResistance - resistanceReduction)));
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void boundless$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(AttributeRegistry.DAMAGE_RESISTANCE);
        cir.getReturnValue().add(AttributeRegistry.TOP_SPEED_MULTIPLIER);
        cir.getReturnValue().add(AttributeRegistry.TIME_UNTIL_MAX_SPEED);
    }

    @Unique
    private float getPostBlockDamage(DamageSource source, PlayerEntity player, float initialAmount) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        float blockHP = stack.getOrDefault(DataComponentRegistry.BLOCK_HP, 100f);

        if (blockHP > 0.0 && BlockLogic.isBlocking(player) && BlockLogic.shouldBlockDamage(source, player)) {
            float blockedDamage = 0.0f;
            float excessDamage = initialAmount;

            if (blockHP > initialAmount) {
                stack.set(DataComponentRegistry.BLOCK_HP, blockHP - initialAmount);
                blockedDamage = initialAmount;
            } else {
                blockedDamage = blockHP;
                stack.set(DataComponentRegistry.BLOCK_HP, Math.max(0.0f, blockHP - initialAmount));
            }

            excessDamage = initialAmount - blockedDamage;
            return excessDamage;
        }

        return initialAmount;
    }
}