package com.boundless.networking.payloads.evasion;

import com.boundless.BoundlessAPI;
import com.boundless.networking.PayloadRegistry;
import com.boundless.util.AnimationUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record EvasionServerPayload(String direction) implements CustomPayload {
    public static final CustomPayload.Id<EvasionServerPayload> ID = new CustomPayload.Id<>(PayloadRegistry.EVASION_SERVER);
    public static final PacketCodec<RegistryByteBuf, EvasionServerPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, EvasionServerPayload::direction, EvasionServerPayload::new);

    public static void receive(EvasionServerPayload payload, ServerPlayNetworking.Context context) {
        PlayerEntity player = context.player();
        String animation = "front_handspring";
        float animationSpeed = 1.0f;

        if (payload.direction.equals("back")) {
            animation = "roll_back";
            animationSpeed = 1.25f;
        } else {
            animation = player.isOnGround() ? "front_handspring" : "roll_forward";
            animationSpeed = player.isOnGround() ? 1.75f : 1.0f;
        }

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier(animation), animationSpeed, false, true, 9999);
        float groundRollDist = 1.5f;
        float rollDistMultiplier = player.isOnGround() ? groundRollDist : groundRollDist / 2;

        Vec3d normalizedRotationVector = player.getRotationVector().normalize().multiply(rollDistMultiplier);
        if (payload.direction.equals("back")) {
            normalizedRotationVector = normalizedRotationVector.multiply(-1);
        }

        player.addVelocity(new Vec3d(normalizedRotationVector.x, 0.25, normalizedRotationVector.z));
        player.velocityDirty = true;
        player.velocityModified = true;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
