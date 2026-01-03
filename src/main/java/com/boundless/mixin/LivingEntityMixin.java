package com.boundless.mixin;

import com.boundless.registry.AttributeRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @ModifyReturnValue(method = "modifyAppliedDamage", at = @At("RETURN"))
    protected float boundless$modifyAppliedDamage(float original) {
        return (float) (original - (original * (this.getAttributeValue(AttributeRegistry.DAMAGE_RESISTANCE) - 1)));
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void boundless$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(AttributeRegistry.DAMAGE_RESISTANCE);
        cir.getReturnValue().add(AttributeRegistry.TOP_SPEED_MULTIPLIER);
        cir.getReturnValue().add(AttributeRegistry.TIME_UNTIL_MAX_SPEED);
    }
}