package com.boundless.hero.shadow_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.gui.HeroHUD;
import com.boundless.hero.shadow_hero.technique.TenShadowsComponents;
import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import com.boundless.mechanics.ComboManager;
import com.boundless.mechanics.ShikigamiManager;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.RaycastUtils;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class GamaPullAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        EntityHitResult result = RaycastUtils.raycast(player, 32);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 32, 1.5f) : result.getEntity();
        if (target == null || target == player) return;

        Vec3d leftSide = player.getRotationVector().crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(-1);
        Vec3d gamaPos = player.getPos().add(leftSide.multiply(1.5));
        GamaEntity gama = ShikigamiManager.getShikigami(player, EntityRegistry.GAMA);

        if (gama != null) {
            ShikigamiManager.desummonShikigami(player, EntityRegistry.GAMA);
        }

        gama = ShikigamiManager.summonShikigamiAt(player, EntityRegistry.GAMA, gamaPos);
        if (gama == null) return;

        gama.setPullTarget(target);
        gama.setPullTimer(5);
        gama.pullTarget(target);
        gama.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, target.getPos());
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("gama_pull");
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.literal("Gama Pull: ").append(ComboManager.formattedComboText("LRR", ComboManager.getProgress(player, TenShadowsComponents.CURRENT_ORDER_SEQUENCE)));
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 240;
    }
}
