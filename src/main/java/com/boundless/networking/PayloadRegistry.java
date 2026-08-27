package com.boundless.networking;

import com.boundless.BoundlessAPI;
import com.boundless.networking.payloads.*;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import com.boundless.networking.payloads.evasion.EvasionServerPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;

public class PayloadRegistry {
    public static final Identifier ABILITY_USE = BoundlessAPI.id("ability_use");
    public static final Identifier ANIMATION_PLAY = BoundlessAPI.id("animation_play");
    public static final Identifier ANIMATION_STOP = BoundlessAPI.id("animation_stop");
    public static final Identifier EVASION_CLIENT = BoundlessAPI.id("evasion_client");
    public static final Identifier EVASION_SERVER = BoundlessAPI.id("evasion_server");
    public static final Identifier UPDATE_HOLD_STATE = BoundlessAPI.id("update_hold_state");
    public static final Identifier CAMERA_SHAKE = BoundlessAPI.id("camera_shake");
    public static final Identifier UPDATE_DRAG = BoundlessAPI.id("update_drag");

    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(AbilityUsePayload.ID, AbilityUsePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(EvasionServerPayload.ID, EvasionServerPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateHoldStatePayload.ID, UpdateHoldStatePayload.CODEC);

        PayloadTypeRegistry.playS2C().register(CameraShakePayload.ID, PacketCodec.unit(new CameraShakePayload()));
        PayloadTypeRegistry.playS2C().register(AnimationPlayPayload.ID, AnimationPlayPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AnimationStopPayload.ID, AnimationStopPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EvasionClientPayload.ID, EvasionClientPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateDragPayload.ID, UpdateDragPayload.CODEC);
    }

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(AbilityUsePayload.ID, AbilityUsePayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(EvasionServerPayload.ID, EvasionServerPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(UpdateHoldStatePayload.ID, UpdateHoldStatePayload::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CameraShakePayload.ID, CameraShakePayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(AnimationPlayPayload.ID, AnimationPlayPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(AnimationStopPayload.ID, AnimationStopPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(EvasionClientPayload.ID, EvasionClientPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(UpdateDragPayload.ID, UpdateDragPayload::receive);
    }
}
