package com.boundless.mixin;

import com.boundless.BoundlessAPI;
import com.boundless.hero.switcher_hero.BoogieLogic;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AnimationUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class EvasionMixin {

    // Todo: make specific to todo

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    public void boundless$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (source.isIn(DamageTypeTags.IS_PROJECTILE) && HeroUtils.isHero(player) && source.getAttacker() != null) {
            AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("clap"), 2.0f, false, false, 3000);
            SoundUtils.playSound(player, SoundRegistry.CLAP_1);
            BoogieLogic.swapEntities(player, source.getAttacker());
            source.getAttacker().damage(source, amount);
            cir.cancel();
        }

    }
}
