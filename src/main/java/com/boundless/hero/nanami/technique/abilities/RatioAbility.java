package com.boundless.hero.nanami.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.nanami.technique.RatioComponents;
import com.boundless.hero.nanami.technique.RatioSkillcheck;
import com.boundless.mechanics.CooldownManager;
import com.boundless.registry.ItemRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RatioAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        if (user.getWorld().isClient) return;

        ItemStack stack = HeroUtils.getHeroStack(user);
        RatioSkillcheck skillcheck = stack.get(RatioComponents.RATIO_SKILLCHECK);

        if (skillcheck == null) {
            long leniency = isUsingCleaver(user) ? 3 : 1;
            float damageMultiplierReward = isUsingCleaver(user) ? 1.5f : 1.75f;

            startSkillcheck(user, 40, 20, leniency, damageMultiplierReward);
            return;
        }

        if (skillcheck.isSuccessful(user.getWorld().getTime())) {
            stack.set(RatioComponents.NEXT_ATTACK_RATIO_MULTIPLIER, skillcheck.nextAttackRatioMultiplier());
        }

        stack.remove(RatioComponents.RATIO_SKILLCHECK);
        CooldownManager.setAbilityCooldownIfHigher(user, this.getId(), 200);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("ratio");
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 5;
    }

    @Override
    public int getCost(LivingEntity user) {
        return 733;
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.of("Ratio");
    }

    private static void startSkillcheck(LivingEntity user, long duration, long ticksUntilTarget, long leniency, float damageMultiplierReward) {
        long startTick = user.getWorld().getTime();

        RatioSkillcheck skillcheck = new RatioSkillcheck(startTick, startTick + duration, startTick + ticksUntilTarget, leniency, damageMultiplierReward);
        HeroUtils.getHeroStack(user).set(RatioComponents.RATIO_SKILLCHECK, skillcheck);
    }

    private static boolean isUsingCleaver(LivingEntity user) {
        return user.getMainHandStack().isOf(ItemRegistry.CLEAVER);
    }
}
