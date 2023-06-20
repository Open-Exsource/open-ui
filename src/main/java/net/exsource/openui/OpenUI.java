package net.exsource.openui;

import net.exsource.openlogger.Logger;

public final class OpenUI {

    private static final Logger logger = Logger.getLogger();

    public static void launch() {
        launch(null);
    }

    public static void launch(String[] args) {
        if(args != null && args.length != 0) {
            logger.info("Program arguments found... check");
            Registry.registerProperties(args);
        } else {
            logger.info("No program arguments found... skip");
        }
    }

}
