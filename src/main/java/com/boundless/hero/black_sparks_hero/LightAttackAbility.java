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

public class LightAttackAbility extends HeldAbility {
    public LightAttackAbility(Consumer<PlayerEntity> abilityLogic, Predicate<PlayerEntity> abilityConditional, int cooldown, int iconHeight, int iconWidth, Identifier abilityIcon, Identifier abilityID, boolean hide, int requiredHoldTime, String keybind) {
        super(abilityLogic, abilityConditional, cooldown, iconHeight, iconWidth, abilityIcon, abilityID, hide, requiredHoldTime, keybind);
    }

    @Override
    public void holdTickLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        KeybindHoldData data = KeybindingUtils.getHoldData(player, this.getKeybind());

        if (data.held()) {
            long heldFor = player.getWorld().getTime() - data.startTimestamp();
            if (heldFor > 3) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 1, false, false, false));
            }
        } else {
            long heldFor = player.getWorld().getTime() - data.startTimestamp();
            KeybindingUtils.endKeybindHold(player, this.getKeybind());

            if (heldFor >= this.getRequiredHoldTime()) {
                Map<Identifier, Long> cooldownData = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
                long cooldownEnd = cooldownData.get(this.getAbilityID());

                if (data.startTimestamp() >= cooldownEnd) {
                    this.getAbilityLogic().accept(player);
                    AbilityUtils.setAbilityCooldown(player, this.getAbilityID(), this.getCooldown());
                }
            } else {
                LightAttackAbility.lightAttack(player);
            }
        }
    }

    public static void lightAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        if (BlackSparksHero.updateMinigameCombo(player, "l")) return;

        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = DataComponentUtils.getInt(DataComponentRegistry.ATTACK_COUNT, player, 0);

        if (CursedEnergyAbility.channelCursedEnergyActive(player)) {
            BlackSparksHero.startMinigame(player, "l");
            HeroUtils.getHeroStack(player).set(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, player.getWorld().getTime());
        }

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
