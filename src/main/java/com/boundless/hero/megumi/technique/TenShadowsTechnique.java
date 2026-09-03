package com.boundless.hero.megumi.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.ability.generic.SummonShikigamiAbility;
import com.boundless.hero.megumi.technique.abilities.GamaGrappleAbility;
import com.boundless.hero.megumi.technique.abilities.GamaPullAbility;
import com.boundless.hero.megumi.technique.abilities.ShikigamiOrderAbility;
import com.boundless.hero.megumi.technique.abilities.ShikigamiOrdersMenuAbility;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.util.math.Vec3d;

public class TenShadowsTechnique {
    public static final TechniqueAbility KURO = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.DIVINE_DOG_KURO, 200, 5));
    public static final TechniqueAbility SHIRO = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.DIVINE_DOG_SHIRO, 200, 5));
    public static final TechniqueAbility GAMA = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.GAMA, 300, 5));
    public static final TechniqueAbility GAMA_GRAPPLE = TechniqueAbilityRegistry.register(new GamaGrappleAbility());
    public static final TechniqueAbility GAMA_PULL = TechniqueAbilityRegistry.register(new GamaPullAbility());
    public static final TechniqueAbility TOGGLE_SHIKIGAMI_ORDERS_MENU = TechniqueAbilityRegistry.register(new ShikigamiOrdersMenuAbility());
    public static final TechniqueAbility SHIKIGAMI_ORDER_LEFT = TechniqueAbilityRegistry.register(new ShikigamiOrderAbility("L"));
    public static final TechniqueAbility SHIKIGAMI_ORDER_RIGHT = TechniqueAbilityRegistry.register(new ShikigamiOrderAbility("R"));

    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(new PunchAbility()
            .setAbilityId(BoundlessAPI.id("megumi_punch"))
            .setDamage(18f)
            .setAttackDuration(8));

    public static final TechniqueAbility ROUNDHOUSE_KICK = TechniqueAbilityRegistry.register(new PunchAbility()
            .setAbilityId(BoundlessAPI.id("megumi_roundhouse"))
            .setAnimation(BoundlessAPI.id("roundhouse"))
            .setDamage(36)
            .setAttackDuration(17)
            .setImpactTick(9)
            .setAnimationSpeed(1.15f)
            .setKnockback(new Vec3d(1.2, 0.6, 1.2)));
}
