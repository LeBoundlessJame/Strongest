package com.boundless.networking.payloads;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.networking.PayloadRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.ComponentUtils;
import com.boundless.util.HeroUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.HashMap;

public record UpdateHoldStatePayload(String key, boolean held) implements CustomPayload {
    public static final CustomPayload.Id<UpdateHoldStatePayload> ID = new CustomPayload.Id<>(PayloadRegistry.UPDATE_HOLD_STATE);

    public static final PacketCodec<RegistryByteBuf, UpdateHoldStatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, UpdateHoldStatePayload::key,
            PacketCodecs.BOOL, UpdateHoldStatePayload::held,
            UpdateHoldStatePayload::new);

    public static void receive(UpdateHoldStatePayload payload, ServerPlayNetworking.Context context) {
        ItemStack heroStack = HeroUtils.getHeroStack(context.player());
        long worldTime = context.player().getWorld().getTime();

        HashMap<String, KeybindHoldData> keybindDataMap = new HashMap<>(heroStack.getOrDefault(DataComponentRegistry.HELD_KEYBIND, new HashMap<String, KeybindHoldData>()));
        KeybindHoldData keybindHoldData = keybindDataMap.getOrDefault(payload.key, new KeybindHoldData(payload.held, context.player().getWorld().getTime(), 0L));

        keybindHoldData = new KeybindHoldData(payload.held, payload.held ? worldTime : keybindHoldData.startTimestamp(), payload.held ? 0L : worldTime);
        ComponentUtils.updateMap(heroStack, DataComponentRegistry.HELD_KEYBIND, payload.key, keybindHoldData);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
