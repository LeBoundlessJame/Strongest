package com.boundless.registry;

import com.boundless.hero.api.Hero;
import com.boundless.hero.megumi.Megumi;
import com.boundless.hero.nanami.Nanami;
import com.boundless.hero.yuji.Yuji;

import java.util.ArrayList;

public class HeroRegistry {
    public static ArrayList<Hero> HEROES = new ArrayList<>();
    public static Hero MEGUMI = new Megumi();
    public static Hero NANAMI = new Nanami();
    public static Hero YUJI = new Yuji();
    public static void initialize() {}
}
