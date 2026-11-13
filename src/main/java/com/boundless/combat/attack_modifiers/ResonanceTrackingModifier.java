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

        HashMap<UUID, ResonanceData> resonanceTracker = (HashMap<UUID, ResonanceData>) ComponentUtils.getOr(player, StrawdollComponents.RESONANCE_TRACKER, new HashMap<>());

        if (!resonanceTracker.containsKey(uuid) || resonanceTracker.get(uuid) == null || resonanceTracker.get(uuid).isExpired(player.getWorld().getTime())) {
            ResonanceData data = new ResonanceData(hit.getDamage(), player.getWorld().getTime());
            ComponentUtils.updateMap(HeroUtils.getHeroStack(player), StrawdollComponents.RESONANCE_TRACKER, uuid, data);
        } else {
            ResonanceData data = resonanceTracker.get(uuid);
            ResonanceData updated = new ResonanceData(data.damageDealt() + hit.getDamage(), player.getWorld().getTime());
            ComponentUtils.updateMap(HeroUtils.getHeroStack(player), StrawdollComponents.RESONANCE_TRACKER, uuid, updated);
        }
    }

    @Override
    public void onTrigger(PlayerEntity player) {

    }
}
