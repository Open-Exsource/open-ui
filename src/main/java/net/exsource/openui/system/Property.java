package net.exsource.openui.system;

import org.jetbrains.annotations.NotNull;

public class Property {

    private final String key;
    private PropertyValue value;

    public Property(@NotNull String key, PropertyValue value) {
        this.key = key;
        this.value = value;
    }

    public Property(@NotNull String key, Object value) {
        this(key, new PropertyValue(value));
    }

    public Property(@NotNull String key) {
        this(key, "none");
    }

    public void setValue(PropertyValue value) {
        this.value = value;
    }

    public void setValue(Object value) {
        this.setValue(new PropertyValue(value));
    }

    public PropertyValue getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
