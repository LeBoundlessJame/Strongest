package com.boundless.networking.payloads;

import com.boundless.networking.PayloadRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record UpdateDragPayload(UUID uuid) implements CustomPayload {
    public static final CustomPayload.Id<UpdateDragPayload> ID = new CustomPayload.Id<>(PayloadRegistry.UPDATE_DRAG);
    public static final PacketCodec<RegistryByteBuf, UpdateDragPayload> CODEC =
            PacketCodec.tuple(Uuids.PACKET_CODEC, UpdateDragPayload::uuid, UpdateDragPayload::new);

    public static void receive(UpdateDragPayload payload, ClientPlayNetworking.Context context) {
        PlayerEntity user = context.player().getWorld().getPlayerByUuid(payload.uuid());
        context.client().execute(() -> {
            if (user.getUuid().equals(context.client().player.getUuid())) {
                user.setNoDrag(false);
            }
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}