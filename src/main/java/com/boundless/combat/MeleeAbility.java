package com.boundless.combat;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.util.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

@Getter @Setter
public class MeleeAbility extends Ability {
    public Identifier animation;
    public float damage;
    public float animationSpeed;
    public int impactTick;
    public BiConsumer<PlayerEntity, HeroActionEntity> attackLogic;

    public MeleeAbility(Identifier id) {
        super(id);
        this.setCooldown(20);
        this.setSkillSlot(3);
        this.setIcon(BoundlessAPI.hudPNG("open"));
    }

    @Override
    public void executeAbility(PlayerEntity player) {
        AnimationUtils.playAlternatingSyncedAnimation(player, this.getAnimation(), this.getAnimationSpeed(), true, 2000);
        Action attack = Action.builder().scheduledTask(this.getImpactTick(),
                (user, action) -> {
                MeleeUtils.basicHit(user, action, damage);
        }).build();
        AttackUtils.startAttackTimer(player, this.getAbilityDuration());
        ActionUtils.performAction(player, attack);
    }

    @Override
    public boolean canUseAbility(PlayerEntity player) {
        boolean canUse = super.canUseAbility(player);
        canUse &= HeroUtils.combatModeEnabled(player);
        return canUse;
    }
}
