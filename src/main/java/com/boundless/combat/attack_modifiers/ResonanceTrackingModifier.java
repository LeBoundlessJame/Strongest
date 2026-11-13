package com.boundless.combat.attack_modifiers;

import com.boundless.combat.AttackModifier;
import com.boundless.combat.Hit;
import com.boundless.hero.nobara.technique.components.ResonanceData;
import com.boundless.hero.nobara.technique.components.StrawdollComponents;
import com.boundless.util.ComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResonanceTrackingModifier implements AttackModifier {
    @Override
    public boolean shouldTrigger(PlayerEntity player) {
        return true;
    }

    @Override
    public void apply(Hit hit) {
        PlayerEntity player = hit.getAttacker();
        UUID uuid = hit.getTarget().getUuid();
        long time = player.getWorld().getTime();

        Map<UUID, ResonanceData> resonanceTracker = ComponentUtils.getOr(player, StrawdollComponents.RESONANCE_TRACKER, new HashMap<>());

        ResonanceData data = resonanceTracker.get(uuid);
        if (data == null || data.isExpired(time)) {
            data = new ResonanceData(hit.getDamage(), time);
        } else {
            data = new ResonanceData(data.damageDealt() + hit.getDamage(), time);
        }

        ComponentUtils.updateMap(HeroUtils.getHeroStack(player), StrawdollComponents.RESONANCE_TRACKER, uuid, data);
    }

    @Override
    public void onTrigger(PlayerEntity player) {

    }
}
