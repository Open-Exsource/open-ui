package net.exsource.open;

import net.exsource.open.ui.component.layout.Pane;
import net.exsource.open.ui.component.shapes.Rectangle;
import net.exsource.open.ui.style.generic.Background;
import net.exsource.open.ui.windows.Window;
import net.exsource.openlogger.Logger;
import net.exsource.openutils.enums.Colors;
import net.exsource.openutils.tools.Color;

import java.util.concurrent.TimeUnit;

public class Example {

    private static final Logger logger = Logger.getLogger();

    public static void main(String[] args) throws InterruptedException {
        Logger.enableDebug(true);
        OpenUI.launch(args);
        Window window = UIFactory.createWindow(Window.class);

        Pane pane = new Pane();
        window.addComponent(pane);

        Rectangle rectangle = new Rectangle(null);
        rectangle.setSize(50);

        Rectangle rectangle2 = new Rectangle(null);
        rectangle2.setSize(50);
        rectangle2.getStyle().setBackground(Background.get(Color.named(Colors.CORAL)));
        pane.addChild(rectangle2);
        pane.addChild(rectangle);

        TimeUnit.SECONDS.sleep(2);
        rectangle.setPositionX(300);
    }

}
