package com.boundless.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SoundEventProvider implements DataProvider {
    protected final FabricDataOutput dataOutput;

    protected SoundEventProvider(FabricDataOutput dataOutput) {
        this.dataOutput = dataOutput;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<String, Supplier<JsonElement>> sounds = new HashMap<>();
        JsonObject json = new JsonObject();

        BiConsumer<String, Supplier<JsonElement>> consumer = (name, supplier) -> {
            Supplier<JsonElement> existing = sounds.put(name, supplier);
            if (existing != null) {
                throw new IllegalStateException("Duplicate sound definition for " + name);
            }
        };

        registerSounds(consumer);

        sounds.forEach((name, supplier) -> {
            json.add(name, supplier.get());
        });

        return DataProvider.writeToPath(writer, json, getSoundFilePath());
    }

    public void registerSounds(BiConsumer<String, Supplier<JsonElement>> consumer) {
    }

    public void add(String sound, BiConsumer<String, Supplier<JsonElement>> consumer) {
        consumer.accept(sound, () -> {
            JsonObject json = new JsonObject();
            JsonArray sounds = new JsonArray();

            sounds.add(dataOutput.getModId() + ":" + sound);
            json.add("sounds", sounds);

            return json;
        });
    }

    // I left the first parameter blank so that it gets it in the root of assets, if anyone is wondering
    private Path getSoundFilePath() {
        return dataOutput.getResolver(DataOutput.OutputType.RESOURCE_PACK, "")
                .resolveJson(Identifier.of(dataOutput.getModId(), "sounds"));
    }

    @Override
    public String getName() {
        return "Sound Definitions";
    }
}
