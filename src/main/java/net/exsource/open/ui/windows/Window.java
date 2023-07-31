package net.exsource.open.ui.windows;

import net.exsource.open.ui.AbstractWindow;

public class Window extends AbstractWindow {

    public Window(String identifier) {
        super(identifier);
        setAllowNVG(true);
        setRenderAtIconified(false);
    }

    @Override
    protected void update(float delta) {

    }

    @Override
    protected void render() {

    }

    @Override
    public void destroy() {

    }
}
