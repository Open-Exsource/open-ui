package net.exsource.openui.events;

import net.exsource.openutils.event.Event;
import net.exsource.openutils.io.controller.PropertiesController;
import org.jetbrains.annotations.NotNull;

public class UIPreLoadEvent extends Event {

    private final Class<?> mainClass;
    private final PropertiesController properties;

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
}
