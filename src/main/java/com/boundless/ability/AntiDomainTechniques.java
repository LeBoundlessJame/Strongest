package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.util.AbilityUtils;
import com.boundless.util.EffekUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class AntiDomainTechniques {
    public static Ability SIMPLE_DOMAIN = AbilityUtils.ability(AntiDomainTechniques::simpleDomain, 5, BoundlessAPI.identifier("simple_domain"), null, "Simple Domain");

    public static void simpleDomain(PlayerEntity player) {
        EffekUtils.playEffect(BoundlessAPI.identifier("simple_domain"), player, player.getPos(), new Vec3d(1, 1, 1));
    }
}
