package com.boundless.registry;

import com.boundless.hero.api.Hero;
import com.boundless.hero.black_sparks_hero.BrawlerHero;
import com.boundless.hero.shadow_hero.ShadowHero;
import com.boundless.hero.shrine_hero.ShrineHero;
import com.boundless.hero.switcher_hero.SwitcherHero;

import java.util.ArrayList;

public class HeroRegistry {
    public static ArrayList<Hero> HEROES = new ArrayList<>();
    public static Hero BLACK_SPARKS_HERO = new BrawlerHero();
    public static Hero SWITCHER_HERO = new SwitcherHero();
    public static Hero SHRINE_HERO = new ShrineHero();
    public static Hero SHADOW_HERO = new ShadowHero();
    public static void initialize() {}
}
