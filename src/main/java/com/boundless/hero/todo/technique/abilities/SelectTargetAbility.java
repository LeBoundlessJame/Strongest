package com.boundless.hero.todo.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.todo.technique.components.BoogieWoogieComponents;
import com.boundless.util.ComponentUtils;
import com.boundless.util.RaycastUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;

public class SelectTargetAbility extends TechniqueAbility {
    public Target target;

    public SelectTargetAbility(Target target) {
        this.target = target;
    }

    @Override
    public void activate(LivingEntity user) {
        EntityHitResult result = RaycastUtils.raycast(user, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(user, 64, 1.5f) : result.getEntity();

        if (target == null || target == user) return;
        ComponentType<Integer> component = this.target.equals(Target.PRIMARY) ? BoogieWoogieComponents.PRIMARY_TARGET_ID : BoogieWoogieComponents.SECONDARY_TARGET_ID;
        ComponentUtils.set(user, component, target.getId());
    }

    @Override
    public Identifier getId() {
        String version = target.equals(Target.PRIMARY) ? "primary" : "secondary";
        return BoundlessAPI.id("select_boogie_woogie_target" + "_" + version);
    }

    public Text getDisplayText(LivingEntity userEntity) {
        return target.equals(Target.PRIMARY) ? Text.of("Select primary target") : Text.of("Select secondary target");
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 5;
    }

    public enum Target {
        PRIMARY,
        SECONDARY
    }
}