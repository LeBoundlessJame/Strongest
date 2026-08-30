package com.boundless.combat;

import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.hero_action.HeroActionEntity;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

@Getter
public class Hit {
    private final PlayerEntity attacker;
    private final LivingEntity target;
    private final TechniqueAbility ability;
    private final HeroActionEntity action;
    @Getter @Setter
    private float damage;
    @Getter @Setter
    private Vec3d knockback;
    @Getter @Setter
    private HitEffects hitEffects;

    public Hit(PlayerEntity attacker, LivingEntity target, TechniqueAbility ability, HeroActionEntity action, float damage, Vec3d knockback, HitEffects hitEffects) {
        this.attacker = attacker;
        this.target = target;
        this.ability = ability;
        this.action = action;
        this.damage = damage;
        this.knockback = knockback;
        this.hitEffects = hitEffects;
    }

    public void multiplyDamage(float multiplier) {
        this.damage = damage * multiplier;
    }

    public void addDamage(float amount) {
        this.damage = damage + amount;
    }
}
