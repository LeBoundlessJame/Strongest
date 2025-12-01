package com.boundless.registry;

import com.boundless.hero.BlackSparksHero;
import com.boundless.hero.MeleeHero;
import com.boundless.hero.SuperHero;
import com.boundless.hero.api.Hero;

import java.util.ArrayList;

public class HeroRegistry {
    public static ArrayList<Hero> HEROES = new ArrayList<>();
    public static Hero MELEE_HERO = new MeleeHero();
    public static Hero SUPER_HERO = new SuperHero();
    public static Hero BLACK_SPARKS_HERO = new BlackSparksHero();
    public static void initialize() {}
}
