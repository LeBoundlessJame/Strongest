package com.boundless.networking;

import com.boundless.BoundlessAPI;
import com.boundless.networking.payloads.AbilityUsePayload;
import com.boundless.networking.payloads.AnimationPlayPayload;
import com.boundless.networking.payloads.CameraShakePayload;
import com.boundless.networking.payloads.UpdateHoldStatePayload;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import com.boundless.networking.payloads.evasion.EvasionServerPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class PayloadRegistry {
    public static final Identifier ABILITY_USE = BoundlessAPI.identifier("ability_use");
    public static final Identifier ANIMATION_PLAY = BoundlessAPI.identifier("animation_play");
    public static final Identifier EVASION_CLIENT = BoundlessAPI.identifier("evasion_client");
    public static final Identifier EVASION_SERVER = BoundlessAPI.identifier("evasion_server");
    public static final Identifier UPDATE_HOLD_STATE = BoundlessAPI.identifier("update_hold_state");
    public static final Identifier CAMERA_SHAKE = BoundlessAPI.identifier("camera_shake");

    public static void registerPayloads() {
        registerC2SPayload(AbilityUsePayload.ID, AbilityUsePayload.CODEC, AbilityUsePayload::receive);
        registerC2SPayload(EvasionServerPayload.ID, EvasionServerPayload.CODEC, EvasionServerPayload::receive);
        registerC2SPayload(UpdateHoldStatePayload.ID, UpdateHoldStatePayload.CODEC, UpdateHoldStatePayload::receive);

        PayloadTypeRegistry.playS2C().register(AnimationPlayPayload.ID, AnimationPlayPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EvasionClientPayload.ID, EvasionClientPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraShakePayload.ID, PacketCodec.unit(new CameraShakePayload()));
    }

    public static <P extends CustomPayload> void registerC2SPayload(CustomPayload.Id<P> ID, PacketCodec<RegistryByteBuf, P> packetCodec, BiConsumer<P, ServerPlayNetworking.Context> receiveMethod) {
        PayloadTypeRegistry.playC2S().register(ID, packetCodec);
        ServerPlayNetworking.registerGlobalReceiver(ID, receiveMethod::accept);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CameraShakePayload.ID, CameraShakePayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(AnimationPlayPayload.ID, AnimationPlayPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(EvasionClientPayload.ID, EvasionClientPayload::receive);
    }
}
