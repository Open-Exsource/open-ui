package net.exsource.openui.events.windows;

import net.exsource.openui.ui.UIWindow;
import net.exsource.openutils.event.Cancelable;
import net.exsource.openutils.event.Event;
import org.jetbrains.annotations.NotNull;

public class WindowCloseEvent implements Event, Cancelable {

    private final UIWindow window;
    private boolean cancelled;

    public WindowCloseEvent(@NotNull UIWindow window) {
        this.window = window;
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    public UIWindow getWindow() {
        return window;
    }

    public <T extends UIWindow> T cast() {
        return window.casted();
    }

}
