package com.boundless.networking.payloads.evasion;

import com.boundless.BoundlessAPI;
import com.boundless.ability.MeleeAbilities;
import com.boundless.combat.CombatSystem;
import com.boundless.networking.PayloadRegistry;
import com.boundless.util.AnimationUtils;
import com.boundless.util.MeleeUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
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
        String direction = payload.direction;

        player.setFireTicks(0);

        Entity raycastEntity = CombatSystem.getRaycastEntity(player, 32, 1.5f);
        if (direction.equals("forward") && raycastEntity != null && raycastEntity.distanceTo(player) > 6) {
            MeleeAbilities.offensiveDash(player);
            return;
        }

        if (direction.equals("right") || direction.equals("left")) {
            sideDodge(player, direction, 1.5f);
        } else {
            dodgeRoll(player, direction, 1.5f);
        }

        player.velocityDirty = true;
        player.velocityModified = true;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void sideDodge(PlayerEntity player, String direction, float distance) {
        Vec3d side = player.getRotationVector().crossProduct(new Vec3d(0, 1, 0))
                .normalize().multiply(distance);

        if (direction.equals("left")) {
            side = side.multiply(-1);
        }

        player.addVelocity(side.x, 0, side.z);
    }

    public static void dodgeRoll(PlayerEntity player, String direction, float distance) {
        String animation = direction.equals("back") ? "roll_back" : (player.isOnGround() ? "front_handspring" : "roll_forward");
        float animationSpeed = direction.equals("back") ? 1.25f : (player.isOnGround() ? 1.75f : 1.0f);
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier(animation), animationSpeed, false, true, 9999);

        Vec3d normalizedRotationVector = player.getRotationVector().normalize()
                .multiply(distance).multiply(direction.equals("back") ? -1 : 1);

        player.addVelocity(new Vec3d(normalizedRotationVector.x, normalizedRotationVector.y, normalizedRotationVector.z));
    }

}
