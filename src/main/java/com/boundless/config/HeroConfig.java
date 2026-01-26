package com.boundless.config;

import com.boundless.BoundlessAPI;
import com.boundless.hero.black_sparks_hero.BlackSparksHeroConfig;
import com.boundless.hero.switcher_hero.SwitcherConfig;
import me.fzzyhmstrs.fzzy_config.config.Config;

public class HeroConfig extends Config {
    public HeroConfig() {
        super(BoundlessAPI.identifier("hero_config"));
    }

    public BlackSparksHeroConfig BLACK_SPARKS_CONFIG = new BlackSparksHeroConfig();
    public SwitcherConfig SWITCHER_CONFIG = new SwitcherConfig();

    @Override
    public int defaultPermLevel() {
        return 4;
    }
}
