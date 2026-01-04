package com.boundless.mixin;

import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @ModifyReturnValue(method = "modifyAppliedDamage", at = @At("RETURN"))
    protected float boundless$modifyAppliedDamage(float original) {
        float damageResistance = (float) (this.getAttributeValue(AttributeRegistry.DAMAGE_RESISTANCE) - 1);
        float resistanceReduction = 0.0f;
        if (this.hasStatusEffect(StatusEffectRegistry.BONE_BREAK_EFFECT)) {
            resistanceReduction = (Objects.requireNonNull(this.getStatusEffect(StatusEffectRegistry.BONE_BREAK_EFFECT)).getAmplifier() + 1) * 0.1f;
            resistanceReduction = Math.clamp(resistanceReduction, 0.0f, damageResistance);
        }
        return (original - (original * (damageResistance - resistanceReduction)));
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void boundless$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(AttributeRegistry.DAMAGE_RESISTANCE);
        cir.getReturnValue().add(AttributeRegistry.TOP_SPEED_MULTIPLIER);
        cir.getReturnValue().add(AttributeRegistry.TIME_UNTIL_MAX_SPEED);
    }
}