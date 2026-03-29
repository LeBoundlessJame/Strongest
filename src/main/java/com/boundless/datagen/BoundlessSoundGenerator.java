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
        add("miss_hit", consumer);
        add("clap_1", consumer);
        add("energy_impact_1", consumer);
        add("energy_impact_2", consumer);
        add("energy_impact_3", consumer);
        add("energy_impact_heavy", consumer);
        add("earth_impact", consumer);
        add("rock_crumbling", consumer);
        add("heavy_cut_1", consumer);
        add("heavy_cut_2", consumer);
        add("heavy_cut_3", consumer);
        add("slash_1", consumer);
        add("slash_2", consumer);
        add("punch_1", consumer);
        add("punch_2", consumer);
    }
}
