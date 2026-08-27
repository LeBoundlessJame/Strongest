package com.boundless.hero.shadow_hero.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.ability.generic.SummonShikigamiAbility;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.hero.shadow_hero.technique.abilities.GamaGrappleAbility;
import com.boundless.hero.shadow_hero.technique.abilities.ShikigamiOrdersAbility;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class TenShadowsTechnique {
    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(PunchAbility.builder()
            .abilityId(BoundlessAPI.id("megumi_punch"))
            .damage(18f)
            .attackDuration(8)
            .whiffSound(SoundRegistry.MISS_HIT)
            .impactSound(SoundRegistry.IMPACT_HEAVY_1)
            .build());

    public static final TechniqueAbility ROUNDHOUSE_KICK = TechniqueAbilityRegistry.register(PunchAbility.builder()
            .abilityId(BoundlessAPI.id("megumi_roundhouse"))
            .animation(BoundlessAPI.id("roundhouse"))
            .damage(36)
            .attackDuration(17)
            .impactTick(9)
            .animationSpeed(1.15f)
            .whiffSound(SoundRegistry.MISS_HIT)
            .impactSound(SoundRegistry.IMPACT_HEAVY_1)
            .knockback(new Vec3d(1.2, 0.6, 1.2))
            .build());

    public static final TechniqueAbility KURO = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.DIVINE_DOG_KURO, 200, 5));
    public static final TechniqueAbility SHIRO = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.DIVINE_DOG_SHIRO, 200, 5));
    public static final TechniqueAbility GAMA = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.GAMA, 300, 5));
    public static final TechniqueAbility GAMA_GRAPPLE = TechniqueAbilityRegistry.register(new GamaGrappleAbility());
    public static final TechniqueAbility SHIKIGAMI_ORDERS = TechniqueAbilityRegistry.register(new ShikigamiOrdersAbility());

    public static Identifier getRightClickAbility(PlayerEntity player) {
        return player.getVehicle() instanceof GamaEntity ? GAMA_GRAPPLE.getAbilityId() : ROUNDHOUSE_KICK.getAbilityId();
    }
}
