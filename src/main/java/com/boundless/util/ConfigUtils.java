package com.boundless.util;

import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class ConfigUtils {
    public static ValidatedInt vInt(int defaultValue) {
        return new ValidatedInt(defaultValue, Integer.MAX_VALUE, Integer.MIN_VALUE, ValidatedNumber.WidgetType.TEXTBOX);
    }

    public static ValidatedInt vInt(int defaultValue, int min, int max) {
        return new ValidatedInt(defaultValue, max, min, ValidatedNumber.WidgetType.TEXTBOX);
    }
}
