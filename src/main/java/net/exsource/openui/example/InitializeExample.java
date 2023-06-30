package net.exsource.openui.example;

import net.exsource.openlogger.Logger;
import net.exsource.openui.OpenUI;
import net.exsource.openui.UIManager;
import net.exsource.openui.annotations.init.Init;
import net.exsource.openui.annotations.init.PostInit;
import net.exsource.openui.annotations.init.PreInit;
import net.exsource.openui.ui.windows.DialogWindow;
import net.exsource.openui.ui.windows.Window;

import java.util.concurrent.TimeUnit;

public class InitializeExample {

    private static final Logger logger = Logger.getLogger();

    public static void main(String[] args) throws InterruptedException {
        Logger.enableDebug(false);
        OpenUI.launch(args);

        Window window = UIManager.createWindow("Test", null, Window.class);
        DialogWindow dialog = UIManager.createWindow("Dialog", null, DialogWindow.class);
        window.addChildWindow(dialog);

        TimeUnit.SECONDS.sleep(5);
        window.removeChildWindow(dialog);
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
