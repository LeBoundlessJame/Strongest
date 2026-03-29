package com.boundless;

import com.boundless.datagen.BoundlessItemModelGenerator;
import com.boundless.datagen.BoundlessSoundGenerator;
import com.boundless.datagen.BoundlessTranslationGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BoundlessAPIDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(BoundlessItemModelGenerator::new);
		pack.addProvider(BoundlessTranslationGenerator::new);
        pack.addProvider(BoundlessSoundGenerator::new);
	}
}
