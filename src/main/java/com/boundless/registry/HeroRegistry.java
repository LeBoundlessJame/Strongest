package com.boundless.registry;

import com.boundless.hero.api.Hero;
import com.boundless.hero.shadow_hero.ShadowHero;

import java.util.ArrayList;

public class HeroRegistry {
    public static ArrayList<Hero> HEROES = new ArrayList<>();
    public static Hero SHADOW_HERO = new ShadowHero();
    public static void initialize() {}
}
