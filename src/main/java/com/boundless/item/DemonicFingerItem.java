package com.boundless.item;

import com.boundless.hero.shrine_hero.ShrineHero;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.ComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DemonicFingerItem extends Item {
    public DemonicFingerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient || (!(user instanceof PlayerEntity player))) return super.finishUsing(stack, world, user);
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0, false, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0, false, false, false));

        if (ShrineHero.canEatFinger(user) && ComponentUtils.getInt(ShrineHero.FINGER_COUNT, player, 1) < 15) {
            HeroUtils.getHeroStack(player).set(ShrineHero.FINGER_COUNT, HeroUtils.getHeroStack(player).getOrDefault(ShrineHero.FINGER_COUNT, 0) + 1);
            ComponentUtils.incrementInt(StrongestComponents.CURSED_ENERGY_RESERVES, player, 1000);
        } else {
            user.kill();
        }

        return super.finishUsing(stack, world, user);
    }
}