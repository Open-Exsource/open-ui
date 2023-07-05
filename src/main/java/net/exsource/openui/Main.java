package net.exsource.openui;

import net.exsource.openui.ui.frame.Window;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        OpenUI.launch(args);

        Window window = UIFactory.createWindow(Window.class);

        window.setResizeable(false);

        TimeUnit.SECONDS.sleep(3);
        window.disable();
    }

}
