package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.util.Shikigami;
import com.boundless.util.ShikigamiUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SummonShikigamiAbility<T extends TameableEntity & Shikigami> extends TechniqueAbility {
    private EntityType<T> shikigamiType;
    private int cooldown;

    public SummonShikigamiAbility(EntityType<T> shikigamiType, int cost, int cooldown) {
        this.shikigamiType = shikigamiType;
        this.cooldown = cooldown;
        this.cost = cost;
    }

    @Override
    public void activate(PlayerEntity playerEntity) {
        ShikigamiUtils.toggleShikigami(playerEntity, this.shikigamiType);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("summon_" + shikigamiType.toString());
    }

    @Override
    public String getDisplayString() {
        return "Summon " + Text.translatable(shikigamiType.getTranslationKey()).getString();
    }

    @Override
    public long getCooldown() {
        return this.cooldown;
    }

    @Override
    public int getCost() {
        return this.cost;
    }
}
