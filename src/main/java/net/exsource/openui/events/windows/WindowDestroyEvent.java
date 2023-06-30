package net.exsource.openui.events.windows;

import net.exsource.openui.ui.AbstractWindow;
import net.exsource.openui.ui.ParentWindow;
import net.exsource.openutils.event.Cancelable;
import net.exsource.openutils.event.Event;

public class WindowDestroyEvent implements Event, Cancelable {

    private final AbstractWindow window;
    private boolean cancelled;

    public WindowDestroyEvent(final AbstractWindow window) {
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

    public <T> T getWindow() {
        return window.casted();
    }

    public boolean canBeUseAsParent() { return getWindow() instanceof ParentWindow; }

}
