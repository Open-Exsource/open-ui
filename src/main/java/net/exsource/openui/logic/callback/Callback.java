package net.exsource.openui.logic.callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for create new Callbacks for window handlings.
 * This class need be implicated in classes to work as Callback handler.
 * @since 1.0.0
 * @author Daniel Ramke
 * @param <T> callback type.
 */
public abstract class Callback<T> {

    private final List<T> callbacks = new ArrayList<>();

    /**
     * Function adds a new callback to the list.
     * @param value new callback.
     */
    public void add(T value) {
        if(value != null) {
            callbacks.add(value);
        }
    }

    /**
     * Function removes a callback from the list.
     * @param value callback to remove.
     */
    public void remove(T value) {
        if(value != null) {
            callbacks.remove(value);
        }
    }

    /**
     * @return List<T> - all current T object in the list.
     */
    public List<T> getCallbacks() {
        return callbacks;
    }
}
