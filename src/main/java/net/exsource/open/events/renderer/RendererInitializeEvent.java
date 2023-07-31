package net.exsource.open.events.renderer;

import net.exsource.open.logic.AbstractRenderer;
import net.exsource.open.logic.Renderer;
import net.exsource.openutils.event.Cancelable;
import net.exsource.openutils.event.Event;
import org.jetbrains.annotations.NotNull;

public class RendererInitializeEvent implements Event, Cancelable {

    private final Renderer renderer;
    private boolean cancelled;

    public RendererInitializeEvent(@NotNull AbstractRenderer renderer) {
        this.renderer = renderer;
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

    public Renderer getRenderer() {
        return renderer;
    }
}
