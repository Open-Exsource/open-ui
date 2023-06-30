package net.exsource.openui.events.windows;

import net.exsource.openui.ui.AbstractWindow;
import net.exsource.openutils.event.Cancelable;
import net.exsource.openutils.event.Event;

public class WindowCreateEvent implements Event, Cancelable {

    private final AbstractWindow window;
    private boolean cancelled;

    public WindowCreateEvent(final AbstractWindow window) {
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


}
