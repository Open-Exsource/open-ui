package net.exsource.open.enums.input;

import org.lwjgl.glfw.GLFW;

/**
 * This enum is created for a list of typical actions.
 * You can choose an action which was performed after hitting an input.
 * This enum used the default GLFW actions for inputs.
 * @since 1.0.0
 * @see GLFW
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public enum Action {

    UNKNOWN(GLFW.GLFW_KEY_UNKNOWN),
    RELEASE(GLFW.GLFW_RELEASE),
    PRESS(GLFW.GLFW_PRESS),
    REPEAT(GLFW.GLFW_REPEAT);

    private final int code;

    /**
     * This constructor generated the enum set.
     * @param code the final identifier code from GLFW.
     */
    Action(int code) {
        this.code = code;
    }

    /**
     * @return the final identifier code.
     */
    public int getCode() {
        return this.code;
    }

    /**
     * @return the name as readable string.
     */
    public String getName() {
        return this.name();
    }

    /**
     * This method found the correct enum to the given name.
     * @param name the name of an action.
     * @return the founded action, UNKNOWN if it doesn't found the action name.
     */
    public static Action get(String name) {
        Action action = UNKNOWN;
        for(Action actions : values()) {
            if(actions.getName().equalsIgnoreCase(name)) {
                action = actions;
            }
        }
        return action;
    }

    /**
     * This method found the correct enum to the given code.
     * @param code the code of an action.
     * @return the founded action, UNKNOWN if it doesn't found the action name.
     */
    public static Action get(int code) {
        Action action = UNKNOWN;
        for(Action actions : values()) {
            if(actions.getCode() == code) {
                action = actions;
            }
        }
        return action;
    }

    /**
     * Override the default toString() method and returned the enum name.
     * @return enum name Uppercase.
     */
    @Override
    public String toString() {
        return this.getName();
    }


}
