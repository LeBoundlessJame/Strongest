package com.boundless.mixin.aaaparticles_fix;

import com.boundless.networking.payloads.AnimationPlayPayload;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import dev.architectury.impl.NetworkAggregator;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import mod.chloeprime.aaaparticles.AAAParticles;
import mod.chloeprime.aaaparticles.common.network.ModNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketDecoder;
import net.minecraft.network.codec.ValueFirstEncoder;
import net.minecraft.network.packet.CustomPayload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Mixin(ModNetwork.class)
public class NetworkMixin {
    @Shadow @Final private static AtomicInteger id;

    @Shadow @Final private static ConcurrentHashMap<Class<?>, CustomPayload.Id<?>> TYPE_TO_ID_MAP;

    // Todo: I'd like to give a huge credit to the https://github.com/Buuz135/FindMe project as I found the fix from them!
    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static <T extends CustomPayload> void boundless$register(NetworkManager.Side side, Class<T> type, ValueFirstEncoder<PacketByteBuf, T> encoder, PacketDecoder<PacketByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkManager.PacketContext>> handler, CallbackInfo ci) {
        CustomPayload.Id<T> payloadType = new CustomPayload.Id(AAAParticles.loc(String.valueOf(id.getAndIncrement())));
        TYPE_TO_ID_MAP.put(type, payloadType);
        PacketCodec<PacketByteBuf, T> codec = PacketCodec.of(encoder, decoder);

        if (Platform.getEnvironment().equals(Env.SERVER)) {
            NetworkAggregator.registerS2CType(payloadType, codec, List.of());
        } else {
            NetworkAggregator.registerReceiver(NetworkManager.s2c(), payloadType, codec, Collections.emptyList(), (packet, context) -> handler.accept(packet, (Supplier)() -> context));
        }
        ci.cancel();
    }

}
