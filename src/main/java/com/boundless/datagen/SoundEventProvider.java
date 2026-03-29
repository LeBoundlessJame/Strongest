package com.boundless.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class SoundEventProvider implements DataProvider {
    protected final FabricDataOutput dataOutput;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;

    protected SoundEventProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this.dataOutput = dataOutput;
        this.registryLookup = registryLookup;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return null;
    }

    // I left the first parameter blank so that it gets it in the root of assets, if anyone is wondering
    private Path getSoundFilePath() {
        return dataOutput.getResolver(DataOutput.OutputType.RESOURCE_PACK, "")
                .resolveJson(Identifier.of(dataOutput.getModId()));
    }

    @Override
    public String getName() {
        return "Sound Definitions";
    }
}
