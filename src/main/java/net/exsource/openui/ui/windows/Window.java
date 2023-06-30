package net.exsource.openui.ui.windows;

import net.exsource.openlogger.Logger;
import net.exsource.openui.ui.ParentWindow;

import java.util.concurrent.TimeUnit;

public class Window extends ParentWindow {

    private static final Logger logger = Logger.getLogger();

    public Window(String serialID) {
        super(serialID);
    }

    public Window() {
        this(null);
    }

    @Override
    protected void initialization() {
        logger.info("Initialized -> " + getSerialID());
    }

    @Override
    protected void update() {

    }

    @Override
    protected void clear() {
        logger.info("Clear -> " + getSerialID());
    }
}
