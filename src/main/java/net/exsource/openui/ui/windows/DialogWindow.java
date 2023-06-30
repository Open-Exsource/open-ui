package net.exsource.openui.ui.windows;

import net.exsource.openlogger.Logger;
import net.exsource.openui.ui.ChildWindow;

public class DialogWindow extends ChildWindow {

    private static final Logger logger = Logger.getLogger();

    public DialogWindow(String serialID) {
        super(serialID);
    }

    @Override
    protected void initialization() {
        logger.info("Initialize Child " + getSerialID() + ", for -> " + getParent().getSerialID());
    }

    @Override
    protected void update() {
        logger.info("Update Child " + getSerialID() + ", for -> " + getParent().getSerialID());
    }

    @Override
    protected void clear() {
        logger.info("Clear Child " + getSerialID() + ", for -> " + getParent().getSerialID());
    }
}
