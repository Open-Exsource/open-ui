package net.exsource.openui;

import net.exsource.openui.ui.component.shapes.Rectangle;
import net.exsource.openui.ui.frame.Window;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        OpenUI.launch(args);

        Window window = UIFactory.createWindow(Window.class);

        Rectangle rectangle = new Rectangle(null);
        window.addComponent(rectangle);
    }

}
