package net.exsource.open.logic.renderer;

import net.exsource.open.logic.renderer.util.NanoVGBackground;
import net.exsource.open.ui.UIWindow;
import net.exsource.open.ui.component.Component;
import net.exsource.open.ui.style.generic.Background;
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
            handler.draw(component.getPositionX(), component.getPositionY(), component.getWidth(), component.getHeight(), background);
        });
    }
}
