package com.boundless.datagen;

import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BoundlessSoundGenerator extends SoundEventProvider {
    public BoundlessSoundGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void registerSounds(BiConsumer<String, Supplier<JsonElement>> consumer) {
        add("impact_heavy_1", consumer);
    }
}
