package com.boundless.networking.payloads;

import com.boundless.networking.PayloadRegistry;
import com.boundless.util.CameraUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;

public record CameraShakePayload() implements CustomPayload {
    public static final CustomPayload.Id<CameraShakePayload> ID = new CustomPayload.Id<>(PayloadRegistry.CAMERA_SHAKE);

    public static void receive(CameraShakePayload payload, ClientPlayNetworking.Context context) {
        CameraUtils.addCameraShake(context.client(), 40, 1.2f);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
