package com.boundless.networking.payloads;

import com.boundless.networking.PayloadRegistry;
import com.boundless.mechanics.AbilityManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AbilityUsePayload(Identifier abilityID) implements CustomPayload {
    public static final CustomPayload.Id<AbilityUsePayload> ID = new CustomPayload.Id<>(PayloadRegistry.ABILITY_USE);

    public static final PacketCodec<RegistryByteBuf, AbilityUsePayload> CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, AbilityUsePayload::abilityID,
            AbilityUsePayload::new);

    public static void receive(AbilityUsePayload payload, ServerPlayNetworking.Context context) {
        AbilityManager.checkAndUseTechniqueAbility(context.player(), payload.abilityID);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
