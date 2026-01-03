package com.boundless.mixin;

import com.boundless.BoundlessAPI;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class ShaderMixin {
    @Shadow
    @Nullable PostEffectProcessor postProcessor;

    @Shadow
    protected abstract void loadPostProcessor(Identifier id);

    @Inject(method = "onCameraEntitySet", at = @At("HEAD"), cancellable = true)
    public void boundless$onCameraEntitySet(Entity entity, CallbackInfo ci) {
        if (this.postProcessor != null) {
            this.postProcessor.close();
        }

        this.postProcessor = null;
        if (entity instanceof CreeperEntity) {
            this.loadPostProcessor(Identifier.ofVanilla("shaders/post/creeper.json"));
        } else if (entity instanceof SpiderEntity) {
            this.loadPostProcessor(Identifier.ofVanilla("shaders/post/spider.json"));
        } else if (entity instanceof EndermanEntity) {
            this.loadPostProcessor(Identifier.ofVanilla("shaders/post/invert.json"));
        } else if (entity instanceof VillagerEntity) {
            this.loadPostProcessor(Identifier.of(BoundlessAPI.MOD_ID, "shaders/post/black_flash.json"));
        }
        ci.cancel();
    }
}
