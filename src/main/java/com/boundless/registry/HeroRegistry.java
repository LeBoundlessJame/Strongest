package com.boundless.registry;

import com.boundless.hero.api.Hero;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
import com.boundless.hero.switcher_hero.SwitcherHero;

import java.util.ArrayList;

public class HeroRegistry {
    public static ArrayList<Hero> HEROES = new ArrayList<>();
    public static Hero BLACK_SPARKS_HERO = new BlackSparksHero();
    public static Hero SWITCHER_HERO = new SwitcherHero();
    public static void initialize() {}
}
