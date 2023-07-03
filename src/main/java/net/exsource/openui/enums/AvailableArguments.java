package net.exsource.openui.enums;

import org.jetbrains.annotations.NotNull;

public enum AvailableArguments {

    LEGACY_MODE("legacy-mode"),
    UI_STYLE("ui-style"),
    UI_MAX_THREADS("ui-max-threads");

    private final String param;

    AvailableArguments(final String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public static boolean isValid(@NotNull String value) {
        boolean valid = false;
        for(AvailableArguments arguments : values()) {
            if(arguments.getParam().equals(value)) {
                valid = true;
                break;
            }
        }
        return valid;
    }
}
