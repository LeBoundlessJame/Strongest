package com.boundless.config;

import com.boundless.BoundlessAPI;
import com.boundless.hero.black_sparks_hero.BrawlerHeroConfig;
import com.boundless.hero.shrine_hero.ShrineConfig;
import com.boundless.hero.switcher_hero.SwitcherConfig;
import me.fzzyhmstrs.fzzy_config.config.Config;

public class HeroConfig extends Config {
    public HeroConfig() {
        super(BoundlessAPI.identifier("hero_config"));
    }

    public BrawlerHeroConfig BLACK_SPARKS_CONFIG = new BrawlerHeroConfig();
    public SwitcherConfig SWITCHER_CONFIG = new SwitcherConfig();
    public ShrineConfig SHRINE_CONFIG = new ShrineConfig();

    @Override
    public int defaultPermLevel() {
        return 4;
    }
}
