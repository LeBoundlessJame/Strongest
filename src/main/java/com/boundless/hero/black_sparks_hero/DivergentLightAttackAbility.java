package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.HeldAbility;
import com.boundless.ability.components.KeybindHoldData;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DivergentLightAttackAbility extends HeldAbility {
    public DivergentLightAttackAbility(Consumer<PlayerEntity> abilityLogic, Predicate<PlayerEntity> abilityConditional, int cooldown, int iconHeight, int iconWidth, Identifier abilityIcon, Identifier abilityID, boolean hide, int requiredHoldTime, String keybind) {
        super(abilityLogic, abilityConditional, cooldown, iconHeight, iconWidth, abilityIcon, abilityID, hide, requiredHoldTime, keybind);
    }

    // Todo: A little messy and repetitive, could do with a small refactor soon
    @Override
    public void holdTickLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!AbilityUtils.canUseAbility(player, this.getAbilityID())) return;

        KeybindHoldData data = KeybindingUtils.getHoldData(player, this.getKeybind());

        long heldFor = player.getWorld().getTime() - data.startTimestamp();

        if (data.held()) {
            Map<Identifier, Long> cooldownData = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
            long cooldownEnd = cooldownData.get(this.getAbilityID());

            if (heldFor > 3 && data.startTimestamp() >= cooldownEnd) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 1, false, false, false));
            }
        } else {
            KeybindingUtils.endKeybindHold(player, this.getKeybind());

            if (heldFor >= this.getRequiredHoldTime()) {
                Map<Identifier, Long> cooldownData = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
                long cooldownEnd = cooldownData.getOrDefault(this.getAbilityID(), player.getWorld().getTime() + 100);

                if (data.startTimestamp() >= cooldownEnd) {
                    this.getAbilityLogic().accept(player);
                    AbilityUtils.setAbilityCooldown(player, this.getAbilityID(), this.getCooldown() * 8L);
                }
            } else {
                DivergentLightAttackAbility.lightAttack(player);
                AbilityUtils.setAbilityCooldown(player, this.getAbilityID(), this.getCooldown());
            }
        }
    }

    public static void lightAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        if (CursedEnergyAbility.updateMinigameCombo(player, "l")) return;

        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = DataComponentUtils.getInt(DataComponentRegistry.ATTACK_COUNT, player, 0);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        BiConsumer<PlayerEntity, HeroActionEntity> hook = (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, BlackSparksHero.DAMAGE.lightAttack.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        };
        tasks.put(4, hook);
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"), 1.0f, attackCount % 2 == 0, true, 2000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        AttackUtils.startAttackTimer(player, 4);
    }
}
