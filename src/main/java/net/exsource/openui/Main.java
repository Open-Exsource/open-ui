package net.exsource.openui;

import net.exsource.openlogger.Logger;
import net.exsource.openui.ui.component.shapes.Rectangle;
import net.exsource.openui.ui.frame.Window;

import java.util.concurrent.TimeUnit;

public class Main {

    private String field;

    public static void main(String[] args) throws InterruptedException {
        OpenUI.launch(args);
        Logger.enableDebug(true);

        Window window = UIFactory.createWindow(Window.class);

        Rectangle rectangle = new Rectangle(null);

        window.addComponent(rectangle);
    }

}
