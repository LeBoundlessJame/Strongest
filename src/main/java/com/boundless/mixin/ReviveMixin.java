package com.boundless.mixin;

import com.boundless.BoundlessAPI;
import com.boundless.hero.switcher_hero.ReviveLogic;
import com.boundless.hero.switcher_hero.SwitcherHero;
import com.boundless.util.EffekUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ReviveMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"), method = "damage", cancellable = true)
    public void boundless$tryUseTotem(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!((LivingEntity) (Object) this instanceof PlayerEntity player)) return;

        if (player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(SwitcherHero.REVIVE_TIME, 0L)) {
            ReviveLogic.revive(player);
            cir.cancel();
        }
    }
}
