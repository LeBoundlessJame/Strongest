package com.boundless.networking.payloads;

import com.boundless.networking.PayloadRegistry;
import com.boundless.util.AnimationUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record AnimationPlayPayload(UUID user, Identifier animation, float speed, boolean mirror, boolean repeatIfPlaying, int priority) implements CustomPayload {
    public static final CustomPayload.Id<AnimationPlayPayload> ID = new CustomPayload.Id<>(PayloadRegistry.ANIMATION_PLAY);
    public static final PacketCodec<RegistryByteBuf, AnimationPlayPayload> CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, AnimationPlayPayload::user,
            Identifier.PACKET_CODEC, AnimationPlayPayload::animation,
            PacketCodecs.FLOAT, AnimationPlayPayload::speed,
            PacketCodecs.BOOL, AnimationPlayPayload::mirror,
            PacketCodecs.BOOL, AnimationPlayPayload::repeatIfPlaying,
            PacketCodecs.INTEGER, AnimationPlayPayload::priority,
            AnimationPlayPayload::new
    );

    public static void receive(AnimationPlayPayload payload, ClientPlayNetworking.Context context) {
        PlayerEntity user = context.player().getWorld().getPlayerByUuid(payload.user);
        if (user == null || context.client() == null) return;

        context.client().execute(() -> {
            AnimationUtils.playClientAnimation(user, payload.animation, payload.speed, payload.mirror, payload.repeatIfPlaying, payload.priority);
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
