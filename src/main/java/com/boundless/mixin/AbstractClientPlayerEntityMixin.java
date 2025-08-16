package com.boundless.mixin;

import com.boundless.hero.SuperHero;
import com.boundless.util.HeroUtils;
import com.boundless.util.IAnimatedHero;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin implements IAnimatedHero {
    @Unique
    private final ModifierLayer<IAnimation> modAnimationContainer = new ModifierLayer<>();
    @Unique
    private Identifier lastTriggeredAnimation = null;
    @Unique
    private HashMap<Identifier, Integer> animationPriorityMap = new HashMap<>();

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientWorld world, GameProfile profile, CallbackInfo ci) {
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(1000, modAnimationContainer);
    }

    @Override
    public ModifierLayer<IAnimation> boundless_getModAnimation() {
        return modAnimationContainer;
    }

    @Override
    public Identifier boundless$getLastTriggeredAnimation() {
        return this.lastTriggeredAnimation;
    }

    @Override
    public void boundless$setLastTriggeredAnimation(Identifier identifier) {
        this.lastTriggeredAnimation = identifier;
    }

    @Override
    public int boundless$getAnimationPriority(Identifier identifier, int defaultValue) {
        return this.animationPriorityMap.getOrDefault(identifier, defaultValue);
    }

    @Override
    public void boundless$setAnimationPriority(Identifier identifier, int priority) {
        this.animationPriorityMap.put(identifier, priority);
    }
}
