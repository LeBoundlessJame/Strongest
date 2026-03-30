package com.boundless.combat;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiConsumer;

@Getter @Setter
public class MeleeAbility extends Ability {
    public BiConsumer<PlayerEntity, HeroActionEntity> attackLogic = this::basicHit;

    public Identifier animation = BoundlessAPI.identifier("hook");
    public float damage = 1f;
    public float animationSpeed = 1f;
    public int impactTick = 4;
    public boolean allowsBlackFlash = true;

    public boolean comboable = true;
    public String comboLetter = "l";

    public MeleeAbility(Identifier id) {
        super(id);
        this.setCooldown(10);
        this.setSkillSlot(3);
        this.setAbilityDuration(10);
        this.setIcon(BoundlessAPI.hudPNG("punch"));
    }

    @Override
    public void executeAbility(PlayerEntity player) {
        AnimationUtils.playAlternatingSyncedAnimation(player, this.getAnimation(), this.getAnimationSpeed(), true, 3000);

        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.LIMITED_SPEED, ConfigRegistry.HERO_CONFIG.COMBAT_CONFIG.sprintSpeedLimitDuration.get(), 0, false, false, false));
        SoundUtils.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 8, 12);

        if (evaluateCombos(player)) return;

        Action attack = Action.builder().scheduledTask(this.getImpactTick(), this.getAttackLogic()).build();

        if (this.allowsBlackFlash) {
            if (BlackFlashUtils.isBlackFlashHit(player)) {
                attack = Action.builder().scheduledTask(this.getImpactTick(), (user, action) -> {
                    BlackFlashUtils.blackFlash(player, this.getDamage() * 1.5f, new Vec3d(4f, 0.5, 4f), action);
                }).build();
            } else {
                BlackFlashUtils.removeZoneIfPresent(player);
            }
        }

        AttackUtils.startAttackTimer(player, this.getAbilityDuration());
        ActionUtils.performAction(player, attack);
    }

    @Override
    public boolean canUseAbility(PlayerEntity player) {
        boolean canUse = super.canUseAbility(player);
        canUse &= HeroUtils.combatModeEnabled(player);
        return canUse;
    }

    public void basicHit(PlayerEntity player, HeroActionEntity action) {
        MeleeUtils.forEach(player, action, (user, entity) -> {
            entity.damage(entity.getDamageSources().generic(), this.getDamage());
            if (!(entity instanceof LivingEntity livingEntity)) return;

            CombatUtils.playImpactVisual(player, livingEntity, BoundlessAPI.identifier("melee_impact"));
            SoundUtils.playSound(player, SoundRegistry.IMPACT_HEAVY_1);
            SoundUtils.playSound(player, player.getRandom().nextBoolean() ? SoundRegistry.PUNCH_1 : SoundRegistry.PUNCH_2, 9, 11);
            MeleeUtils.knockback(user, livingEntity, new Vec3d(0.5f, 0.3, 0.5f));
        });
    }

    /** returns if a combo has been triggered or not **/
    public boolean evaluateCombos(PlayerEntity player) {
        HeroData heroData = HeroUtils.getHeroData(player);
        if (heroData == null) return false;
        boolean evaluated = false;
        for (Combo combo: heroData.getCombos()) {
            if (combo.matchesTargetCombo(player, this.getComboLetter())) {
                evaluated = true;
            }
            combo.updateAndEvaluateCombo(player, this.getComboLetter());
            if (evaluated) return evaluated;
        }
        return false;
    }
}
