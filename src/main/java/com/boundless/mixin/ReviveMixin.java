package com.boundless.mixin;

import com.boundless.hero.api.HeroData;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ReviveMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"), method = "damage", cancellable = true)
    public void boundless$tryUseTotem(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!((LivingEntity) (Object) this instanceof PlayerEntity player)) return;
        if (!HeroUtils.isHero(player)) return;
        HeroData heroData = HeroUtils.getHeroData(player);
        if (heroData == null) return;

        if (heroData.getCustomReviveLogic() != null) {
            heroData.getCustomReviveLogic().accept(player, null);
            cir.cancel();
        }

        // Todo: fix revive for ONLY switcher

        /*

        if (player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(SwitcherHero.TIME_UNTIL_NEXT_REVIVE, 0L)) {
            ReviveLogic.revive(player);
            cir.cancel();
        }

         */
    }
}
