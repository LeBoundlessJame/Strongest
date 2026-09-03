package com.boundless.registry;

import com.boundless.hero.api.Hero;
import com.boundless.hero.nanami.Nanami;
import com.boundless.hero.megumi.Megumi;

import java.util.ArrayList;

public class HeroRegistry {
    public static ArrayList<Hero> HEROES = new ArrayList<>();
    public static Hero SHADOW_HERO = new Megumi();
    public static Hero RATIO_TECHNIQUE_HERO = new Nanami();
    public static void initialize() {}
}
