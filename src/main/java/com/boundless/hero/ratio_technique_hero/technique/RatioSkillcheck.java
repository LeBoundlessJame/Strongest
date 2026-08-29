package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.loadouts.TechniqueLoadoutComponent;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;

public record RatioSkillcheck(long startTick, long endTick, long targetTick, long leniency) {
    public static final Codec<RatioSkillcheck> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(Codec.LONG.fieldOf("start_tick")
                .forGetter(RatioSkillcheck::startTick), Codec.LONG.fieldOf("endTick")
                .forGetter(RatioSkillcheck::endTick), Codec.LONG.fieldOf("target_tick")
                .forGetter(RatioSkillcheck::targetTick), Codec.LONG.fieldOf("leniency")
                .forGetter(RatioSkillcheck::leniency)).apply(builder, RatioSkillcheck::new);
    });

    public boolean isSuccessful(long attemptTick) {
        return !isExpired(attemptTick) && attemptTick >= targetTick - leniency && attemptTick <= targetTick + leniency;
    }

    public boolean isExpired(long currentTick) {
        return currentTick > endTick;
    }
}
