package net.exsource.openui.logic.renderer;

import net.exsource.openui.ui.UIWindow;
import net.exsource.openui.ui.component.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UIBackgroundRenderer extends UIRenderer {

    public UIBackgroundRenderer() {
        super(null);
    }

    @Override
    public void load(UIWindow window) {

    }

    @Override
    public void render(@NotNull List<Component> components) {
        System.out.println("Renderer -> " + getName() + ", Size -> " + components.size());
    }
}
