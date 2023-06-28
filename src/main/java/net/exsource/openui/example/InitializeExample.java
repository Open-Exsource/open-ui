package net.exsource.openui.example;

import net.exsource.openlogger.Logger;
import net.exsource.openui.OpenUI;
import net.exsource.openui.annotations.init.Init;
import net.exsource.openui.annotations.init.PostInit;
import net.exsource.openui.annotations.init.PreInit;

public class InitializeExample {

    private static final Logger logger = Logger.getLogger();

    public static void main(String[] args) {
        Logger.enableDebug(true);
        OpenUI.launch(args);
    }

    @PreInit
    public void preInitialization() {
        logger.info("Pre initialization state is reached!");
    }

    @Init
    public void initialization() {
        logger.info("Initialization state is reached!");
    }

    @PostInit
    public void postInitialization() {
        logger.info("Post initialization state is reached!");
    }
}
