package com.boundless.hero.shrine_hero;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class ShrineConfig extends ConfigSection {
    public ShrineConfig() {
        super();
    }

    public ValidatedFloat damageReduction = new ValidatedFloat(0.8f, 1.0f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);

    public ShrineConfig.AbilityDamageConfig ABILITY_DAMAGE_CONFIG = new ShrineConfig.AbilityDamageConfig();
    public ShrineConfig.AbilityCooldownConfig ABILITY_COOLDOWN_CONFIG = new ShrineConfig.AbilityCooldownConfig();
    public ShrineConfig.DomainConfig DOMAIN_CONFIG = new ShrineConfig.DomainConfig();
    public ShrineConfig.AbilityCostConfig ABILITY_COST_CONFIG = new ShrineConfig.AbilityCostConfig();

    public static class AbilityCooldownConfig extends ConfigSection {
        public ValidatedInt lightAttack = new ValidatedInt(5, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt mediumAttack = new ValidatedInt(10, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt cleave = new ValidatedInt(400, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt dismantle = new ValidatedInt(5, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt open = new ValidatedInt(1200, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt domainExpansion = new ValidatedInt(6000, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
    }

    public static class DomainConfig extends ConfigSection {
        public ValidatedInt domainDuration = new ValidatedInt(600, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt initialDelay = new ValidatedInt(60, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt timeBetweenSlashes = new ValidatedInt(1, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt timeBetweenSlashVFX = new ValidatedInt(5, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt timeBetweenMobChecks = new ValidatedInt(20, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt timeBetweenShaderApplications = new ValidatedInt(20, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat weakestSlashDamage = new ValidatedFloat(4.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat strongestSlashDamage = new ValidatedFloat(12.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
    }

    public static class AbilityDamageConfig extends ConfigSection {
        public ValidatedFloat weakestLightAttack = new ValidatedFloat(12.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat strongestLightAttack = new ValidatedFloat(22.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);

        public ValidatedFloat weakestMediumAttackPerHit = new ValidatedFloat(6.5f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat strongestMediumAttackPerHit = new ValidatedFloat(12.5f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);

        public ValidatedFloat weakestDismantle = new ValidatedFloat(8.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat strongestDismantle = new ValidatedFloat(20.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);

        public ValidatedFloat weakestMaxCleaveDamage = new ValidatedFloat(100.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat strongestMaxCleaveDamage = new ValidatedFloat(1000.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);

        public ValidatedFloat weakestOpenDamage = new ValidatedFloat(200.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat strongestOpenDamage = new ValidatedFloat(500.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);

        public ValidatedFloat weakestFurnaceArrowDamage = new ValidatedFloat(800.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat strongestFurnaceArrowDamage = new ValidatedFloat(1000.0f, 99999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
    }

    public static class AbilityCostConfig extends ConfigSection {
        public ValidatedFloat dismantlePercentageCost = new ValidatedFloat(0.04f, 1.0f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat cleavePercentageCost = new ValidatedFloat(0.2f, 1.0f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat openPercentageCost = new ValidatedFloat(0.3f, 1.0f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedFloat domainExpansionCost = new ValidatedFloat(0.5f, 1.0f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
    }
}
