package com.boundless.hero.todo.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.todo.technique.components.BoogieWoogieComponents;
import com.boundless.util.ComponentUtils;
import com.boundless.util.RaycastUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;

public class SelectTargetAbility extends TechniqueAbility {
    public Target target;

    public SelectTargetAbility(Target target) {
        this.target = target;
    }

    @Override
    public void activate(PlayerEntity player) {
        EntityHitResult result = RaycastUtils.raycast(player, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 64, 1.5f) : result.getEntity();

        if (target == null || target == player) return;
        ComponentType<Integer> component = this.target.equals(Target.PRIMARY) ? BoogieWoogieComponents.PRIMARY_TARGET_ID : BoogieWoogieComponents.SECONDARY_TARGET_ID;
        ComponentUtils.set(player, component, target.getId());
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("select_boogie_woogie_target");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 5;
    }

    public enum Target {
        PRIMARY,
        SECONDARY
    }
}