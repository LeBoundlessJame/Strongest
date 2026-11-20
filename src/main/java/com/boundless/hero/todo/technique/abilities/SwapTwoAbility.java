package com.boundless.hero.todo.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.todo.technique.components.BoogieWoogieComponents;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class SwapTwoAbility extends BoogieWoogieAbility {
    @Override
    public void activate(LivingEntity user) {
        Integer primary = ComponentUtils.getOr(user, BoogieWoogieComponents.PRIMARY_TARGET_ID, null);
        Integer secondary = ComponentUtils.getOr(user, BoogieWoogieComponents.SECONDARY_TARGET_ID, null);

        if (primary == null || secondary == null) return;

        Entity primaryEntity = user.getWorld().getEntityById(primary);
        Entity secondaryEntity = user.getWorld().getEntityById(secondary);

        if (primaryEntity == null || secondaryEntity == null) return;

        clap(user);
        swapEntities(primaryEntity, secondaryEntity);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("boogie_woogie_swap_two");
    }

    public int getCost(LivingEntity user) {
        return 100;
    }

    public long getCooldown(LivingEntity user) {
        return 10;
    }

    public Text getDisplayText(LivingEntity userEntity) {
        return Text.of("Boogie Woogie - Swap Two");
    }
}
