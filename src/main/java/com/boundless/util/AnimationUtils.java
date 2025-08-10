package com.boundless.util;

import com.boundless.BoundlessAPI;
import com.boundless.networking.payloads.AnimationPlayPayload;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import dev.kosmx.playerAnim.minecraftApi.layers.LeftHandedHelperModifier;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

import java.util.Objects;

public class AnimationUtils {

    /**
     * Plays an animation and sends a packet for multiplayer display
     **/
    public static void playAnimation(PlayerEntity user, Identifier animation, float speed, boolean mirror, boolean important) {
        //playClientAnimation(user, animation);
        if (user.getWorld().isClient) return;

        for (ServerPlayerEntity target : PlayerLookup.tracking((ServerWorld) user.getWorld(), new ChunkPos((int) user.getPos().x / 16, (int) user.getPos().z / 16))) {
            ServerPlayNetworking.send(target, new AnimationPlayPayload(user.getUuid(), animation, speed, mirror, important));
        }
    }

    public static void playAnimation(PlayerEntity user, Identifier animation) {
        playAnimation(user, animation, 1.0f, false, true);
    }

    public static void playAnimation(PlayerEntity user, Identifier animation, boolean important) {
        playAnimation(user, animation, 1.0f, false, important);
    }

    public static void playClientAnimation(PlayerEntity user, Identifier animation, float speed) {
        playClientAnimation(user, animation, speed, false);
    }

    public static void playClientAnimation(PlayerEntity user, Identifier animation, float speed, boolean mirror) {
        playClientAnimation(user, animation, speed, mirror, true);
    }

    /** If marked as unimportant, it will not interrupt the current animation. **/
    public static void playClientAnimation(PlayerEntity user, Identifier animation, float speed, boolean mirror, boolean important) {
        if (user.getWorld().isClient) {
            var playerAnimationContainer = ((IAnimatedHero) user).boundless_getModAnimation();

            if (!important) {
                if (playerAnimationContainer.isActive()) return;
            }

            if (animation.equals(BoundlessAPI.identifier("null"))) {
                playerAnimationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTCIRC), null);
                return;
            }

            KeyframeAnimation anim = (KeyframeAnimation) PlayerAnimationRegistry.getAnimation(animation);
            var builder = anim.mutableCopy();
            anim = builder.build();
            var animationContainer = new ModifierLayer<IAnimation>();

            animationContainer.addModifierBefore(new SpeedModifier(speed));
            animationContainer.addModifierBefore(new MirrorModifier(mirror));
            animationContainer.addModifierBefore(new LeftHandedHelperModifier(user));

            animationContainer.setAnimation(new KeyframeAnimationPlayer(anim).setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftArm(true)));
            playerAnimationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTCIRC), animationContainer);
        }
    }
}
