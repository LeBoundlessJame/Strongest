package com.boundless.hero.switcher_hero;

import com.boundless.hero.black_sparks_hero.BlackSparksHeroConfig;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedLong;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class SwitcherConfig extends ConfigSection {
    public SwitcherConfig() {
        super();
    }

    public ValidatedFloat damageReduction = new ValidatedFloat(0.80f, 1.0f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
    public ValidatedInt impactFrameDuration = new ValidatedInt(5, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);

    public SwitcherConfig.AbilityDamageConfig abilityDamageConfig = new SwitcherConfig.AbilityDamageConfig();
    public SwitcherConfig.AbilityCooldownConfig abilityCooldownConfig = new SwitcherConfig.AbilityCooldownConfig();

    public static class AbilityCooldownConfig extends ConfigSection {
        public ValidatedInt lightAttack = new ValidatedInt(5, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt mediumAttack = new ValidatedInt(10, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt clap = new ValidatedInt(5, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt targetSelect = new ValidatedInt(5, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt rockThrow = new ValidatedInt(400, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
    }

    public static class AbilityDamageConfig extends ConfigSection {
        public ValidatedFloat lightAttack = new ValidatedFloat(18.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat mediumAttackPerHit = new ValidatedFloat(12.5f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat blackFlash = new ValidatedFloat(180.0f, 99999, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
    }
}
