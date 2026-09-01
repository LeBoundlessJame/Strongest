package com.boundless.hero.ratio_technique_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.ratio_technique_hero.technique.RatioComponents;
import com.boundless.hero.ratio_technique_hero.technique.RatioSkillcheck;
import com.boundless.mechanics.CooldownManager;
import com.boundless.registry.ItemRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RatioAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        ItemStack stack = HeroUtils.getHeroStack(player);
        RatioSkillcheck skillcheck = stack.get(RatioComponents.RATIO_SKILLCHECK);

        if (skillcheck == null) {
            long leniency = isUsingCleaver(player) ? 3 : 1;
            float damageMultiplierReward = isUsingCleaver(player) ? 1.75f : 2.0f;

            startSkillcheck(player, 40, 20, leniency, damageMultiplierReward);
            return;
        }

        if (skillcheck.isSuccessful(player.getWorld().getTime())) {
            stack.set(RatioComponents.NEXT_ATTACK_RATIO_MULTIPLIER, skillcheck.nextAttackRatioMultiplier());
        }

        stack.remove(RatioComponents.RATIO_SKILLCHECK);
        CooldownManager.setAbilityCooldownIfHigher(player, this.getAbilityId(), 200);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("ratio");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 5;
    }

    @Override
    public int getCost(PlayerEntity player) {
        return 233;
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Ratio");
    }

    private static void startSkillcheck(PlayerEntity player, long duration, long ticksUntilTarget, long leniency, float damageMultiplierReward) {
        long startTick = player.getWorld().getTime();

        RatioSkillcheck skillcheck = new RatioSkillcheck(startTick, startTick + duration, startTick + ticksUntilTarget, leniency, damageMultiplierReward);
        HeroUtils.getHeroStack(player).set(RatioComponents.RATIO_SKILLCHECK, skillcheck);
    }

    private static boolean isUsingCleaver(PlayerEntity player) {
        return player.getMainHandStack().isOf(ItemRegistry.CLEAVER);
    }
}
