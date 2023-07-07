package net.exsource.openui.logic.input;

import net.exsource.openui.enums.input.Action;
import net.exsource.openui.enums.input.Input;

/**
 * This class is used for interact with keyboard.
 * This is important for interact with windows or components.
 * @since 1.0.0
 * @see Action
 * @see Input
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public class Keyboard {

    private static Keyboard instance;

    private final boolean[] keys;
    private boolean wait;

    /**
     * @return this class as static object.
     */
    private static Keyboard get() {
        if(instance == null) {
            instance = new Keyboard();
        }
        return instance;
    }

    /**
     * Is a private constructor to generate the static get function.
     */
    private Keyboard() {
        this.keys = new boolean[350];
        this.wait = false;
    }

    /**
     * This is an input callback.
     * @param windowID the window witch control the callback.
     * @param key the key witch is pressed.
     * @param scancode the scancode of the keyboard.
     * @param action the action of the typing like RELEASE or PRESSED.
     * @param mods the mods define if it CTRL + A or something.
     */
    public static void callback(long windowID, int key, int scancode, int action, int mods) {
        if(action == Action.PRESS.getCode()) {
            if(!(get().wait)) {
                get().keys[key] = true;
            }
            get().wait = true;
        } else if(action == Action.RELEASE.getCode()) {
            get().keys[key] = false;
            get().wait = false;
        }
    }

    /**
     * @return if shift pressed or not.
     */
    public static boolean isShiftPressed() {
        return input(Input.SHIFT_LEFT) | input(Input.SHIFT_RIGHT);
    }

    /**
     * @return is control pressed or not.
     */
    public static boolean isControlPressed() {
        return input(Input.CONTROL_LEFT) | input(Input.CONTROL_RIGHT);
    }

    /**
     * @return is alt pressed or not.
     */
    public static boolean isAltPressed() {
        return input(Input.ALT_LEFT) | input(Input.ALT_RIGHT);
    }

    /**
     * This method set a trigger for a key of the keyboard.
     * @param input the input type witch was detected.
     * @return the state of the key, true if it pressed.
     */
    public static boolean input(Input input) {
        return get().keys[input.getCode()];
    }

}
