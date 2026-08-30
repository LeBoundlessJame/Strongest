package com.boundless.combat;

import com.boundless.entity.hero_action.HeroActionEntity;
import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

@Getter
public class Hit {
    private final PlayerEntity attacker;
    private final LivingEntity target;
    private final HeroActionEntity action;
    private float damage;
    private Vec3d knockback;

    public Hit(PlayerEntity attacker, LivingEntity target, HeroActionEntity action, float damage, Vec3d knockback) {
        this.attacker = attacker;
        this.target = target;
        this.action = action;
        this.damage = damage;
        this.knockback = knockback;
    }
}
