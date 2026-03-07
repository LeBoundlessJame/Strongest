package com.boundless.mixin;

import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerPreventionMixin {

    // Todo: make this configurable in the future
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void boundless$update(PlayerEntity player, CallbackInfo ci) {
        if (!HeroUtils.isHero(player)) return;
        HungerManager hungerManager = ((HungerManager) (Object) this);
        hungerManager.setFoodLevel(20);
        hungerManager.setSaturationLevel(20);
        hungerManager.setExhaustion(0);
        ci.cancel();
    }
}
