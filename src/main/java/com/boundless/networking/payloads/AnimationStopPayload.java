package com.boundless.networking.payloads;

import com.boundless.networking.PayloadRegistry;
import com.boundless.util.PlayerAnimationUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record AnimationStopPayload(UUID user, Map<Identifier, Integer> animations) implements CustomPayload {
    public static final Id<AnimationStopPayload> ID = new Id<>(PayloadRegistry.ANIMATION_STOP);
    public static final PacketCodec<RegistryByteBuf, AnimationStopPayload> CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, AnimationStopPayload::user,
            PacketCodecs.map(HashMap::new, Identifier.PACKET_CODEC, PacketCodecs.INTEGER), AnimationStopPayload::animations,
            AnimationStopPayload::new
    );

    public static void receive(AnimationStopPayload payload, ClientPlayNetworking.Context context) {
        PlayerEntity user = context.player().getWorld().getPlayerByUuid(payload.user);
        if (user == null || context.client() == null) return;

        context.client().execute(() -> {
            PlayerAnimationUtils.stopAnimationIfPresent(user, (HashMap<Identifier, Integer>) payload.animations);
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
