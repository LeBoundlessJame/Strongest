package com.boundless.hero.todo;

import com.boundless.BoundlessAPI;
import com.boundless.attribute.AttributeValue;
import com.boundless.combat.CombatAbilities;
import com.boundless.combat.attack_modifiers.BlackFlashModifier;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.todo.technique.TodoTechnique;
import com.boundless.hero.yuji.technique.YujiComponents;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class Todo extends Hero {
    public Todo() {
        YujiComponents.initialize();

        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ATTACK, CombatAbilities.PUNCH)
                .ability(AbilityKey.ABILITY_ONE, TodoTechnique.BOOGIE_WOOGIE)
                .ability(AbilityKey.ABILITY_TWO, CombatAbilities.DROPKICK)
                .ability(AbilityKey.USE, CombatAbilities.ROUNDHOUSE_KICK).build();

        this.heroData = HeroData.builder()
                .name("todo")
                .defaultTechniqueLoadout(loadout)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("todo"))
                .textureIdentifier(BoundlessAPI.textureID("todo"))
                .tickHandler(Hero::onHeroTick)
                .maxCursedEnergy(5000)
                .blackFlashChance(0.04f)
                .blackFlashDamageMultiplier(2.0f)
                .meleeStrength(22)
                .max_health(new AttributeValue(630, EntityAttributeModifier.Operation.ADD_VALUE))
                .size(new AttributeValue(1.1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE))
                .attackModifier(new BlackFlashModifier())
                .build();

        this.registerHero();
    }
}
