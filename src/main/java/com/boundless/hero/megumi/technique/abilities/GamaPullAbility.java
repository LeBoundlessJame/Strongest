package com.boundless.hero.megumi.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.hero.megumi.technique.TenShadowsComponents;
import com.boundless.mechanics.ComboManager;
import com.boundless.mechanics.ShikigamiManager;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.RaycastUtils;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class GamaPullAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        EntityHitResult result = RaycastUtils.raycast(user, 32);
        Entity target = result == null ? RaycastUtils.thickRaycast(user, 32, 1.5f) : result.getEntity();
        if (target == null || target == user) return;

        Vec3d leftSide = user.getRotationVector().crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(-1);
        Vec3d gamaPos = user.getPos().add(leftSide.multiply(1.5));
        GamaEntity gama = ShikigamiManager.getShikigami(user, EntityRegistry.GAMA);

        if (gama != null) {
            ShikigamiManager.desummonShikigami(user, EntityRegistry.GAMA);
        }

        gama = ShikigamiManager.summonShikigamiAt(user, EntityRegistry.GAMA, gamaPos);
        if (gama == null) return;

        gama.setPullTarget(target);
        gama.setPullTimer(5);
        gama.pullTarget(target);
        gama.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, target.getPos());
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("gama_pull");
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.literal("Gama Pull: ").append(ComboManager.formattedComboText("LRR", ComboManager.getProgress(user, TenShadowsComponents.CURRENT_ORDER_SEQUENCE)));
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 240;
    }
}
