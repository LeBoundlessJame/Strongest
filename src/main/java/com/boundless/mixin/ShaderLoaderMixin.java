package com.boundless.mixin;

import com.boundless.util.interfaces.ShaderAccessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public abstract class ShaderLoaderMixin implements ShaderAccessor {
    @Shadow
    public abstract void disablePostProcessor();

    @Shadow
    protected abstract void loadPostProcessor(Identifier id);

    public void boundless$disablePostProcessor() {
        this.disablePostProcessor();
    }

    public void boundless$loadShader(Identifier identifier) {
        this.loadPostProcessor(identifier);
    }
}
