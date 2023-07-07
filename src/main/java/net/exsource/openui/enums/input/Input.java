package net.exsource.openui.enums.input;

import org.lwjgl.glfw.GLFW;

/**
 * This enum contains all used input types of a mouse and keyboard.
 * If you need the size for your keyboard you can get the size by this enum.
 * All of these inputs a catch by glfw.
 * @since 1.0.0
 * @see GLFW
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public enum Input {

    /* Key Board Input */
    UNKNOWN(GLFW.GLFW_KEY_UNKNOWN),
    ENTER(GLFW.GLFW_KEY_ENTER),
    SPACE(GLFW.GLFW_KEY_SPACE),
    BACKSPACE(GLFW.GLFW_KEY_BACKSPACE),
    DELETE(GLFW.GLFW_KEY_DELETE),
    ESCAPE(GLFW.GLFW_KEY_ESCAPE),
    TAB(GLFW.GLFW_KEY_TAB),
    APOSTROPHE(GLFW.GLFW_KEY_APOSTROPHE),
    BACKSLASH(GLFW.GLFW_KEY_BACKSLASH),
    CAPS(GLFW.GLFW_KEY_CAPS_LOCK),
    COMMA(GLFW.GLFW_KEY_COMMA),
    END(GLFW.GLFW_KEY_END),
    PAGE_UP(GLFW.GLFW_KEY_PAGE_UP),
    PAGE_DOWN(GLFW.GLFW_KEY_PAGE_DOWN),
    UP(GLFW.GLFW_KEY_UP),
    DOWN(GLFW.GLFW_KEY_DOWN),
    LEFT(GLFW.GLFW_KEY_LEFT),
    RIGHT(GLFW.GLFW_KEY_RIGHT),
    ALT_LEFT(GLFW.GLFW_KEY_LEFT_ALT),
    ALT_RIGHT(GLFW.GLFW_KEY_RIGHT_ALT),
    LEFT_BRACKET(GLFW.GLFW_KEY_LEFT_BRACKET),
    RIGHT_BRACKET(GLFW.GLFW_KEY_RIGHT_BRACKET),
    CONTROL_LEFT(GLFW.GLFW_KEY_LEFT_CONTROL),
    CONTROL_RIGHT(GLFW.GLFW_KEY_RIGHT_CONTROL),
    SHIFT_LEFT(GLFW.GLFW_KEY_LEFT_SHIFT),
    SHIFT_RIGHT(GLFW.GLFW_KEY_RIGHT_SHIFT),
    SUPER_LEFT(GLFW.GLFW_KEY_LEFT_SUPER),
    SUPER_RIGHT(GLFW.GLFW_KEY_RIGHT_SUPER),
    EQUAL(GLFW.GLFW_KEY_EQUAL),
    GRAVE_ACCENT(GLFW.GLFW_KEY_GRAVE_ACCENT),
    HOME(GLFW.GLFW_KEY_HOME),
    NUM_LOCK(GLFW.GLFW_KEY_NUM_LOCK),
    INSERT(GLFW.GLFW_KEY_INSERT),
    LAST(GLFW.GLFW_KEY_LAST),
    MENU(GLFW.GLFW_KEY_MENU),
    PAUSE(GLFW.GLFW_KEY_PAUSE),
    MINUS(GLFW.GLFW_KEY_MINUS),
    SLASH(GLFW.GLFW_KEY_SLASH),
    SCROLL_LOCK(GLFW.GLFW_KEY_SCROLL_LOCK),
    PRINT(GLFW.GLFW_KEY_PRINT_SCREEN),
    WORLD_1(GLFW.GLFW_KEY_WORLD_1),
    WORLD_2(GLFW.GLFW_KEY_WORLD_2),
    SEMICOLON(GLFW.GLFW_KEY_SEMICOLON),
    PERIOD(GLFW.GLFW_KEY_PERIOD),
    F1(GLFW.GLFW_KEY_F1),
    F2(GLFW.GLFW_KEY_F2),
    F3(GLFW.GLFW_KEY_F3),
    F4(GLFW.GLFW_KEY_F4),
    F5(GLFW.GLFW_KEY_F5),
    F6(GLFW.GLFW_KEY_F6),
    F7(GLFW.GLFW_KEY_F7),
    F8(GLFW.GLFW_KEY_F8),
    F9(GLFW.GLFW_KEY_F9),
    F10(GLFW.GLFW_KEY_F10),
    F11(GLFW.GLFW_KEY_F11),
    F12(GLFW.GLFW_KEY_F12),
    F13(GLFW.GLFW_KEY_F13),
    F14(GLFW.GLFW_KEY_F14),
    F15(GLFW.GLFW_KEY_F15),
    F16(GLFW.GLFW_KEY_F16),
    F17(GLFW.GLFW_KEY_F17),
    F18(GLFW.GLFW_KEY_F18),
    F19(GLFW.GLFW_KEY_F19),
    F20(GLFW.GLFW_KEY_F20),
    F21(GLFW.GLFW_KEY_F21),
    F22(GLFW.GLFW_KEY_F22),
    F23(GLFW.GLFW_KEY_F23),
    F24(GLFW.GLFW_KEY_F24),
    F25(GLFW.GLFW_KEY_F25),
    K_0(GLFW.GLFW_KEY_0),
    K_1(GLFW.GLFW_KEY_1),
    K_2(GLFW.GLFW_KEY_2),
    K_3(GLFW.GLFW_KEY_3),
    K_4(GLFW.GLFW_KEY_4),
    K_5(GLFW.GLFW_KEY_5),
    K_6(GLFW.GLFW_KEY_6),
    K_7(GLFW.GLFW_KEY_7),
    K_8(GLFW.GLFW_KEY_8),
    K_9(GLFW.GLFW_KEY_9),
    NUM_0(GLFW.GLFW_KEY_KP_0),
    NUM_1(GLFW.GLFW_KEY_KP_1),
    NUM_2(GLFW.GLFW_KEY_KP_2),
    NUM_3(GLFW.GLFW_KEY_KP_3),
    NUM_4(GLFW.GLFW_KEY_KP_4),
    NUM_5(GLFW.GLFW_KEY_KP_5),
    NUM_6(GLFW.GLFW_KEY_KP_6),
    NUM_7(GLFW.GLFW_KEY_KP_7),
    NUM_8(GLFW.GLFW_KEY_KP_8),
    NUM_9(GLFW.GLFW_KEY_KP_9),
    A(GLFW.GLFW_KEY_A),
    B(GLFW.GLFW_KEY_B),
    C(GLFW.GLFW_KEY_C),
    D(GLFW.GLFW_KEY_D),
    E(GLFW.GLFW_KEY_E),
    F(GLFW.GLFW_KEY_F),
    G(GLFW.GLFW_KEY_G),
    H(GLFW.GLFW_KEY_H),
    I(GLFW.GLFW_KEY_I),
    J(GLFW.GLFW_KEY_J),
    K(GLFW.GLFW_KEY_K),
    L(GLFW.GLFW_KEY_L),
    M(GLFW.GLFW_KEY_M),
    N(GLFW.GLFW_KEY_N),
    O(GLFW.GLFW_KEY_O),
    P(GLFW.GLFW_KEY_P),
    Q(GLFW.GLFW_KEY_Q),
    R(GLFW.GLFW_KEY_R),
    S(GLFW.GLFW_KEY_S),
    T(GLFW.GLFW_KEY_T),
    U(GLFW.GLFW_KEY_U),
    V(GLFW.GLFW_KEY_V),
    W(GLFW.GLFW_KEY_W),
    X(GLFW.GLFW_KEY_X),
    Y(GLFW.GLFW_KEY_Y),
    Z(GLFW.GLFW_KEY_Z),
    NUM_ADD(GLFW.GLFW_KEY_KP_ADD),
    NUM_DECIMAL(GLFW.GLFW_KEY_KP_DECIMAL),
    NUM_DIVIDE(GLFW.GLFW_KEY_KP_DIVIDE),
    NUM_ENTER(GLFW.GLFW_KEY_KP_ENTER),
    NUM_EQUAL(GLFW.GLFW_KEY_KP_EQUAL),
    NUM_MULTIPLY(GLFW.GLFW_KEY_KP_MULTIPLY),
    NUM_SUBTRACT(GLFW.GLFW_KEY_KP_SUBTRACT),
    /* Mouse Input */
    MOUSE_BUTTON_LEFT(GLFW.GLFW_MOUSE_BUTTON_1),
    MOUSE_BUTTON_MIDDLE(GLFW.GLFW_MOUSE_BUTTON_3),
    MOUSE_BUTTON_RIGHT(GLFW.GLFW_MOUSE_BUTTON_2);

    private final int code;

    /**
     * The initialized constructor for our inputs.
     *
     * @param code the input code as int value.
     */
    Input(int code) {
        this.code = code;
    }

    /**
     * This method give us the code to the given input.
     *
     * @return the input code.
     */
    public final int getCode() {
        return this.code;
    }

    /**
     * This method give us the input name.
     *
     * @return named input.
     */
    public final String getName() {
        return this.name();
    }

    /**
     * @return the complete size of this enum array.
     */
    public static int getSize() {
        return values().length;
    }

    /**
     * This method give us the correct input type by the code.
     * If the code doesn't found, the return is UNKNOWN.
     *
     * @param code to searched input code.
     * @return the input type found by code.
     */
    public static Input get(int code) {
        Input input = UNKNOWN;
        for (Input inputs : values()) {
            if (inputs.getCode() == code) {
                input = inputs;
            }
        }
        return input;
    }

    /**
     * This method give us the correct input type by the name.
     * If the name doesn't found, the return is UNKNOWN.
     *
     * @param name to searched input name.
     * @return the input type found by name.
     */
    public static Input get(String name) {
        Input input = UNKNOWN;
        for (Input inputs : values()) {
            if (inputs.getName().equalsIgnoreCase(name)) {
                input = inputs;
            }
        }
        return input;
    }

    /**
     * Override the default toString() method and returned the enum name.
     *
     * @return enum name Uppercase.
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
