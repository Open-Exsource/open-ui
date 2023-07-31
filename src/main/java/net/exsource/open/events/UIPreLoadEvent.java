package net.exsource.open.events;

import net.exsource.openutils.event.Cancelable;
import net.exsource.openutils.event.Event;
import net.exsource.openutils.io.controller.PropertiesController;
import org.jetbrains.annotations.NotNull;

public class UIPreLoadEvent implements Event, Cancelable {

    private final Class<?> mainClass;
    private final PropertiesController properties;
    private boolean cancelled;

    public UIPreLoadEvent(@NotNull Class<?> mainClass, PropertiesController properties) {
        this.mainClass = mainClass;
        this.properties = properties;
    }

    public Class<?> getMainClass() {
        return mainClass;
    }

    public String getClassName() {
        return getMainClass().getSimpleName();
    }

    public PropertiesController getProperties() {
        return properties;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
