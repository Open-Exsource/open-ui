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
        Rectangle child = new Rectangle(null);

        Rectangle rectangle2 = new Rectangle(null);

        rectangle.addChild(child);
        window.addComponent(rectangle);

        TimeUnit.SECONDS.sleep(3);
        rectangle.addChild(rectangle2);
    }

}
