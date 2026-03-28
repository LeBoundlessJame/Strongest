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
    public Identifier animation = BoundlessAPI.identifier("hook");
    public float damage = 1f;
    public float animationSpeed = 1f;
    public int impactTick = 4;
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
                if (attackLogic == null) {
                    MeleeUtils.basicHit(user, action, damage);
                } else {
                    attackLogic.accept(user, action);
                }
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
