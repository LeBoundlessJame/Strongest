package com.boundless.registry;

import com.boundless.combat.CombatAbilities;
import com.boundless.hero.api.Hero;
import com.boundless.hero.inumaki.Inumaki;
import com.boundless.hero.megumi.Megumi;
import com.boundless.hero.nanami.Nanami;
import com.boundless.hero.nobara.Nobara;
import com.boundless.hero.sukuna.Sukuna;
import com.boundless.hero.todo.Todo;
import com.boundless.hero.yuji.Yuji;

import java.util.ArrayList;

public class HeroRegistry {
    public static ArrayList<Hero> HEROES = new ArrayList<>();
    public static Hero MEGUMI = new Megumi();
    public static Hero NANAMI = new Nanami();
    public static Hero YUJI = new Yuji();
    public static Hero TODO = new Todo();
    public static Hero SUKUNA = new Sukuna();
    public static Hero INUMAKI = new Inumaki();
    public static Hero NOBARA = new Nobara();
    public static void initialize() {}
}
