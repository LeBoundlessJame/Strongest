package com.boundless.registry;

import com.boundless.hero.api.Hero;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;

import java.util.ArrayList;

public class HeroRegistry {
    public static ArrayList<Hero> HEROES = new ArrayList<>();
    public static Hero BLACK_SPARKS_HERO = new BlackSparksHero();
    public static void initialize() {}
}
