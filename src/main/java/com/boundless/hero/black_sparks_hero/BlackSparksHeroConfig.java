package com.boundless.hero.black_sparks_hero;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedLong;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class BlackSparksHeroConfig extends ConfigSection {
    public BlackSparksHeroConfig() {
        super();
    }

    public ValidatedFloat damageReduction = new ValidatedFloat(0.80f, 1.0f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
    public ValidatedLong blackFlashTimeWindow = new ValidatedLong(100, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
    public ValidatedInt impactFrameDuration = new ValidatedInt(60, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);

    public AbilityDamageConfig abilityDamageConfig = new AbilityDamageConfig();
    public AbilityCooldownConfig abilityCooldownConfig = new AbilityCooldownConfig();

    public static class AbilityCooldownConfig extends ConfigSection {
        public ValidatedInt lightAttack = new ValidatedInt(5, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt mediumAttack = new ValidatedInt(5, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt spinKick = new ValidatedInt(20, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt dodge = new ValidatedInt(60, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt blackFlash = new ValidatedInt(200, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
    }

    public static class AbilityDamageConfig extends ConfigSection {
        public ValidatedFloat lightAttack = new ValidatedFloat(20.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat mediumAttackPerHit = new ValidatedFloat(12.5f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat spinKick = new ValidatedFloat(30.0f, 99999, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat blackFlash = new ValidatedFloat(100.0f, 99999, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat divergentFistPunch = new ValidatedFloat(15.0f, 99999, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat divergentFistImpact = new ValidatedFloat(25.0f, 99999, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
    }
}
