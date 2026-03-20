package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.util.AbilityUtils;

public class AntiDomainTechniques {
    public static Ability SIMPLE_DOMAIN = AbilityUtils.ability(SimpleDomain::toggleSimpleDomain, 5, BoundlessAPI.identifier("simple_domain"), null, "Simple Domain");
}
