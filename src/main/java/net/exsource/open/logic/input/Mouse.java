package net.exsource.open.logic.input;

import net.exsource.open.enums.input.Action;

/**
 * This class allowed the interaction with mouse.
 * The mouse can be used for interact with window and components.
 * @since 1.0.0
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public class Mouse {

    private static Mouse instance;

    private double scrollX;

    private double scrollY;

    private double positionX, lastX;

    private double positionY, lastY;

    private final boolean[] buttons;

    private boolean dragging;

    private boolean entered;

    /**
     * @return Mouse - class as static object.
     */
    private static Mouse get() {
        if(instance == null) {
            instance = new Mouse();
        }
        return instance;
    }

    /**
     * Is a private constructor to generate the static get function.
     */
    private Mouse() {
        this.scrollX = 0.0D;
        this.scrollY = 0.0D;
        this.positionX = 0.0D;
        this.positionY = 0.0D;
        this.lastX = 0.0D;
        this.lastY = 0.0D;
        this.buttons = new boolean[3];
        this.dragging = false;
        this.entered = false;
    }

    /**
     * This is a position callback.
     * @param windowID the window witch is control this callback.
     * @param positionX the screen x location.
     * @param positionY the screen y location.
     */
    public static void positionCallback(long windowID, double positionX, double positionY) {
        get().lastX = get().positionX;
        get().lastY = get().positionY;
        get().positionX = positionX;
        get().positionY = positionY;
        get().dragging = get().buttons[0] | get().buttons[1] | get().buttons[2];
    }

    /**
     * This is a mouse entered callback.
     * @param windowID the window witch is control the callback.
     * @param entered the state is true if the mouse entered the window border.
     */
    public static void enteredCallback(long windowID, boolean entered) {
        get().entered = entered;
    }

    /**
     * This is a scroll callback.
     * @param windowID the window witch is control the callback.
     * @param offsetX the scroll x position.
     * @param offsetY the scroll y position.
     */
    public static void scrollCallback(long windowID, double offsetX, double offsetY) {
        get().scrollX = offsetX;
        get().scrollY = offsetY;
    }

    /**
     * This is a click callback.
     * @param windowID the window witch is control the callback.
     * @param button the button code witch was detected.
     * @param action the action of the typing like RELEASE or PRESSED.
     * @param mods the mods define if it CTRL + A or something.
     */
    public static void callback(long windowID, int button, int action, int mods) {
        if(action == Action.PRESS.getCode()) {
            if(button < get().buttons.length) {
                get().buttons[button] = true;
            }
        } else if(action == Action.RELEASE.getCode()) {
            if(button < get().buttons.length) {
                get().buttons[button] = false;
                get().dragging = false;
            }
        }
    }

    /**
     * This method reset the complete mouse handler.
     */
    public static void reset() {
        get().scrollX = 0.0D;
        get().scrollY = 0.0D;
        get().lastX = get().positionX;
        get().lastY = get().positionY;
    }

    /**
     * @return float - screen x position.
     */
    public static float getPositionX() {
        return (float) get().positionX;
    }

    /**
     * @return float - screen y position.
     */
    public static float getPositionY() {
        return (float) get().positionY;
    }

    /**
     * @return float - last screen x position.
     */
    public static float getLastX() {
        return (float) get().lastX;
    }

    /**
     * @return float - last screen y position.
     */
    public static float getLastY() {
        return (float) get().lastY;
    }

    /**
     * @return float - deference screen x position.
     */
    public static float getDeferenceX() {
        return (float) (get().lastX - get().positionX);
    }

    /**
     * @return float - deference screen y position.
     */
    public static float getDeferenceY() {
        return (float) (get().lastY - get().positionY);
    }

    /**
     * @return float - scroll x position.
     */
    public static float getScrollX() {
        return (float) get().scrollX;
    }

    /**
     * @return float - scroll y position.
     */
    public static float getScrollY() {
        return (float) get().scrollY;
    }

    /**
     * @return boolean - if the mouse dragging or not.
     */
    public static boolean isDragging() {
        return get().dragging;
    }

    /**
     * @return boolean - the state of mouse entered or exiting.
     */
    public static boolean isEnteredStage() {
        return get().entered;
    }

}