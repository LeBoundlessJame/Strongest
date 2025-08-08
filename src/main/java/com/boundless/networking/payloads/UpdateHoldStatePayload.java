package com.boundless.networking.payloads;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.networking.PayloadRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UpdateHoldStatePayload(String key, boolean held) implements CustomPayload {
    public static final CustomPayload.Id<UpdateHoldStatePayload> ID = new CustomPayload.Id<>(PayloadRegistry.UPDATE_HOLD_STATE);

    public static final PacketCodec<RegistryByteBuf, UpdateHoldStatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, UpdateHoldStatePayload::key,
            PacketCodecs.BOOL, UpdateHoldStatePayload::held,
            UpdateHoldStatePayload::new);

    public static void receive(UpdateHoldStatePayload payload, ServerPlayNetworking.Context context) {
        ItemStack heroStack = HeroUtils.getHeroStack(context.player());
        KeybindHoldData keybindHoldData = new KeybindHoldData(payload.held, context.player().getWorld().getTime());
        System.out.println("Map before: " + heroStack.get(DataComponentRegistry.HELD_KEYBIND));
        DataComponentUtils.updateMap(heroStack, DataComponentRegistry.HELD_KEYBIND, payload.key, keybindHoldData);
        System.out.println("Map after: " + heroStack.get(DataComponentRegistry.HELD_KEYBIND));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
