package net.exsource.open.ui;

import net.exsource.open.ui.component.Component;

/**
 * Interface for force resizeable functions to the implemented class.
 * Is used at {@link UIWindow} and {@link Component}.
 * @since 1.0.0
 * @author Daniel Ramke
 */
public interface ResizeAble {

    /**
     * Function to set the resizable state.
     * @param resizeAble the new state.
     */
    void setResizeable(boolean resizeAble);

    /**
     * @return {@link Boolean} - the current resizeable state.
     */
    boolean isResizeable();

}
