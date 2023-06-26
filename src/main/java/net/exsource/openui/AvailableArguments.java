package net.exsource.openui;

public enum AvailableArguments {

    UI_STYLE("ui-style"),
    UI_LOGIC("ui-logic"),
    UI_MAX_THREADS("ui-max-threads");

    private final String param;

    AvailableArguments(final String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}
