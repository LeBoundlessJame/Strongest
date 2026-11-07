package com.boundless.hero.todo.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.todo.technique.components.BoogieWoogieComponents;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class SwapTwoAbility extends BoogieWoogieAbility {
    @Override
    public void activate(PlayerEntity player) {
        Integer primary = ComponentUtils.getOr(player, BoogieWoogieComponents.PRIMARY_TARGET_ID, null);
        Integer secondary = ComponentUtils.getOr(player, BoogieWoogieComponents.SECONDARY_TARGET_ID, null);

        if (primary == null || secondary == null) return;

        Entity primaryEntity = player.getWorld().getEntityById(primary);
        Entity secondaryEntity = player.getWorld().getEntityById(secondary);

        if (primaryEntity == null || secondaryEntity == null) return;

        clap(player);
        swapEntities(primaryEntity, secondaryEntity);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("boogie_woogie_swap_two");
    }

    public int getCost(PlayerEntity player) {
        return 100;
    }

    public long getCooldown(PlayerEntity player) {
        return 10;
    }

    public Text getDisplayText(PlayerEntity playerEntity) {
        return Text.of("Boogie Woogie - Swap Two");
    }
}
