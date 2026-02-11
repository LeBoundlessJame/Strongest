package com.boundless;

import com.boundless.ability.Grab;
import com.boundless.networking.PayloadRegistry;
import com.boundless.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoundlessAPI implements ModInitializer {
	public static final String MOD_ID = "boundless";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ItemRegistry.initialize();
		CodecRegistry.initialize();
		RenderLogicRegistry.initialize();
		ConfigRegistry.initialize();
		DataComponentRegistry.initialize();
        Grab.initialize();
		HeroRegistry.initialize();
		PayloadRegistry.registerPayloads();
		PayloadRegistry.registerC2SPackets();
		EntityRegistry.initialize();
		SoundRegistry.initialize();
		StatusEffectRegistry.initialize();
		ParticleRegistry.initialize();
		ItemGroupRegistry.initialize();
		AttributeRegistry.initialize();
		AbilityRegistry.initialize();
        DamageTypeRegistry.initialize();
		LOGGER.info("Boundless API Initialized");
	}

	public static Identifier identifier(String name) {
		return Identifier.of(BoundlessAPI.MOD_ID, name);
	}

	public static Identifier hudPNG(String name) {
		return BoundlessAPI.identifier("textures/gui/sprites/hud/" + name + ".png");
	}
	public static Identifier textureID(String name) {
		return Identifier.of(BoundlessAPI.MOD_ID, "textures/item/hero/" + name + ".png");
	}
	public static Identifier modelID(String name) {
		return Identifier.of(BoundlessAPI.MOD_ID, "geo/item/" + name + ".geo.json");
	}
}