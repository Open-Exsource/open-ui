package net.exsource.openui.ui.component;

import net.exsource.openlogger.Logger;

public abstract class Component {

    private static final Logger logger = Logger.getLogger();

    private final String ID;
    private String localizedName;

    public Component(String ID) {
        this.ID = ID;
        this.localizedName = getClass().getSimpleName();
    }

    public String getID() {
        return ID;
    }

    public String getLocalizedName() {
        return localizedName;
    }
}
