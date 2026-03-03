package com.boundless.config;

import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class CombatConfig extends ConfigSection {
    public CombatConfig() {
        super();
    }

    public ValidatedFloat maxCombatSprintSpeed = new ValidatedFloat(0.15f, 999999f, 0.0f, ValidatedNumber.WidgetType.TEXTBOX);
    public ValidatedInt sprintSpeedLimitDuration = new ValidatedInt(100, 999999, 0, ValidatedNumber.WidgetType.TEXTBOX);
}
