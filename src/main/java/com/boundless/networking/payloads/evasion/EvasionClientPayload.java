package com.boundless.networking.payloads.evasion;

import com.boundless.networking.PayloadRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record EvasionClientPayload(UUID user) implements CustomPayload {
    public static final CustomPayload.Id<EvasionClientPayload> ID = new CustomPayload.Id<>(PayloadRegistry.EVASION_CLIENT);

    public static final PacketCodec<RegistryByteBuf, EvasionClientPayload> CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, EvasionClientPayload::user,
            EvasionClientPayload::new
    );

    public static void receive(EvasionClientPayload payload, ClientPlayNetworking.Context context) {
        PlayerEntity user = context.player().getWorld().getPlayerByUuid(payload.user());
        MinecraftClient client = context.client();

        client.execute(() -> {
            if (user == null || client.player == null || user.getUuid().equals(client.player.getUuid())) return;
            String direction = client.options.forwardKey.isPressed() ? "forward" : "back";
            ClientPlayNetworking.send(new EvasionServerPayload(direction));
        });
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
