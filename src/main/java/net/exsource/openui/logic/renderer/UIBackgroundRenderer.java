package net.exsource.openui.logic.renderer;

import net.exsource.openui.logic.renderer.util.NanoVGBackground;
import net.exsource.openui.style.generic.Background;
import net.exsource.openui.ui.UIWindow;
import net.exsource.openui.ui.component.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UIBackgroundRenderer extends UIRenderer {

    private NanoVGBackground handler;

    public UIBackgroundRenderer() {
        super(null);
    }

    @Override
    public void load(UIWindow window) {
        handler = new NanoVGBackground(window);
    }

    @Override
    public void render(@NotNull List<Component> components) {
        components.forEach(component -> {
            Background background = component.getStyle().getBackground();
            handler.draw(component.getAbsoluteX(), component.getAbsoluteY(), component.getWidth(), component.getHeight(), background);
        });
    }
}
