package net.exsource.openui.logic.callback;

import net.exsource.openui.ui.UIWindow;
import net.exsource.openutils.event.Event;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.glfw.GLFWWindowIconifyCallbackI;
import org.lwjgl.glfw.GLFWWindowMaximizeCallbackI;
import org.lwjgl.glfw.GLFWWindowPosCallbackI;
import org.lwjgl.glfw.GLFWWindowRefreshCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

/**
 * Class for store all known callbacks of GLFW. This callbacks will be converted to
 * {@link Callback} type, this is needed to work with them. The original usage is in
 * {@link UIWindow} for handling window callbacks. Note that this class can not more than
 * storage callbacks. If you wish to use other callbacks as our then you need to create
 * the callbacks by your own. We ar not supporting other callbacks from third party libraries.
 * You need help by callbacks or events like {@link Event}? Then visit our website
 * <a href="https://www.exsource.de/">click here</a>.
 * @since 1.0.0
 * @see Callback
 * @see GLFW
 * @author Daniel Ramke
 */
public class Callbacks {

    /**
     * Callback for handling close action from the calling window.
     * This callback can be used one time, after closing is the callback removed!
     */
    public static class WindowCloseCallback extends Callback<GLFWWindowCloseCallbackI> implements GLFWWindowCloseCallbackI {
        @Override
        public void invoke(long window) {
            for(GLFWWindowCloseCallbackI callback : getCallbacks()) {
                callback.invoke(window);
            }
        }
    }

    /**
     * Callback for handling size action from the calling window.
     * Note that this callback can called multiply times. This can be
     * useful for updates or something, however don't initialize thinks here because
     * it wil override it every time the window change his size!
     */
    public static class WindowSizeCallback extends Callback<GLFWWindowSizeCallbackI> implements GLFWWindowSizeCallbackI {
        @Override
        public void invoke(long window, int width, int height) {
            for(GLFWWindowSizeCallbackI callback : getCallbacks()) {
                callback.invoke(window, width, height);
            }
        }
    }

    /**
     * Callback for handling position action from the calling window.
     * Note that this callback can called multiply times. This can be
     * useful for updates or something, however don't initialize thinks here because
     * it wil override it every time the window change his size!
     */
    public static class WindowPositionCallback extends Callback<GLFWWindowPosCallbackI> implements GLFWWindowPosCallbackI {
        @Override
        public void invoke(long window, int positionX, int positionY) {
            for(GLFWWindowPosCallbackI callback : getCallbacks()) {
                callback.invoke(window, positionX, positionY);
            }
        }
    }

    /**
     * Callback for handling focused state action from the calling window.
     * Note that this is calling if the window become focus or lost his focus.
     * Lost focus is if the user click on the desktop or other program ui's.
     */
    public static class WindowFocusCallback extends Callback<GLFWWindowFocusCallbackI> implements GLFWWindowFocusCallbackI {
        @Override
        public void invoke(long window, boolean focused) {
            for(GLFWWindowFocusCallbackI callback : getCallbacks()) {
                callback.invoke(window, focused);
            }
        }
    }

    /**
     * Callback for handling maximize state action from the calling window.
     * Note this is calling every time you maximize or minimize your window.
     */
    public static class WindowMaximizedCallback extends Callback<GLFWWindowMaximizeCallbackI> implements GLFWWindowMaximizeCallbackI {
        @Override
        public void invoke(long window, boolean maximized) {
            for(GLFWWindowMaximizeCallbackI callback : getCallbacks()) {
                callback.invoke(window, maximized);
            }
        }
    }

    /**
     * Callback for handling iconified state action from the calling window.
     * Note this is called if you iconified the current window to the task bar.
     */
    public static class WindowIconifyCallback extends Callback<GLFWWindowIconifyCallbackI> implements GLFWWindowIconifyCallbackI {
        @Override
        public void invoke(long window, boolean iconified) {
            for(GLFWWindowIconifyCallbackI callback : getCallbacks()) {
                callback.invoke(window, iconified);
            }
        }
    }

    /**
     * Callback for handling restore state action from the calling window.
     * The restoring is called after restore from maximize or iconified.
     * It can call multiply times if you not handle it correctly.
     */
    public static class WindowRefreshCallback extends Callback<GLFWWindowRefreshCallbackI> implements GLFWWindowRefreshCallbackI {
        @Override
        public void invoke(long window) {
            for(GLFWWindowRefreshCallbackI callback : getCallbacks()) {
                callback.invoke(window);
            }
        }
    }

    /**
     * Callback for handling size of framebuffer action from the calling window.
     * Note that this callback can called multiply times. This can be
     * useful for updates or something, however don't initialize thinks here because
     * it wil override it every time the window change his size!
     */
    public static class FrameBufferSizeCallback extends Callback<GLFWFramebufferSizeCallbackI> implements GLFWFramebufferSizeCallbackI {
        @Override
        public void invoke(long window, int width, int height) {
            for(GLFWFramebufferSizeCallbackI callback : getCallbacks()) {
                callback.invoke(window, width, height);
            }
        }
    }

    /**
     * Callback for handling keyboard inputs for the current window.
     * Is called every time the user hits a key of his keyboard.
     * Note that is not called if he hits mouse button's there is an own
     * callback handle {@link MouseButtonCallback}.
     */
    public static class KeyCallback extends Callback<GLFWKeyCallbackI> implements GLFWKeyCallbackI {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            for(GLFWKeyCallbackI callback : getCallbacks()) {
                callback.invoke(window, key, scancode, action, mods);
            }
        }
    }

    /**
     * Callback for handling keyboard inputs for the current window.
     * Is the same as {@link KeyCallback}, but it gives you the correct char which
     * is behind the key.
     */
    public static class CharCallback extends Callback<GLFWCharCallbackI> implements GLFWCharCallbackI {
        @Override
        public void invoke(long window, int codepoint) {
            for(GLFWCharCallbackI callback : getCallbacks()) {
                callback.invoke(window, codepoint);
            }
        }
    }

    /**
     * Callback for handling keyboard inputs which ar called combinations for the current window.
     * This is used for interact with CTRL + C as example. If only called when the user
     * try to hit combos.
     */
    public static class CharModsCallback extends Callback<GLFWCharModsCallbackI> implements GLFWCharModsCallbackI {
        @Override
        public void invoke(long window, int codepoint, int mods) {
            for(GLFWCharModsCallbackI callback : getCallbacks()) {
                callback.invoke(window, codepoint, mods);
            }
        }
    }

    /**
     * Callback for handling mouse inputs for the current window.
     * If the user clicked a mouse button this callback is being called.
     */
    public static class MouseButtonCallback extends Callback<GLFWMouseButtonCallbackI> implements GLFWMouseButtonCallbackI {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            for(GLFWMouseButtonCallbackI callback : getCallbacks()) {
                callback.invoke(window, button, action, mods);
            }
        }
    }

    /**
     * Callback for handling mouse movement for the current window.
     * Called if the user moves the mouse over the window. Is triggered
     * with the {@link MouseEnteredCallback}.
     */
    public static class MousePositionCallback extends Callback<GLFWCursorPosCallbackI> implements GLFWCursorPosCallbackI {
        @Override
        public void invoke(long window, double positionX, double positionY) {
            for(GLFWCursorPosCallbackI callback : getCallbacks()) {
                callback.invoke(window, positionX, positionY);
            }
        }
    }

    /**
     * Callback for handling mouse enter state for the current window.
     * Called if the user moves the mouse to the window and entered or exiting it. Is triggered
     * with the {@link MousePositionCallback}.
     */
    public static class MouseEnteredCallback extends Callback<GLFWCursorEnterCallbackI> implements GLFWCursorEnterCallbackI {
        @Override
        public void invoke(long window, boolean entered) {
            for(GLFWCursorEnterCallbackI callback : getCallbacks()) {
                callback.invoke(window, entered);
            }
        }
    }

    /**
     * Callback for handling mouse scroll position for the current window.
     * Called if the user with the mouse in the window and begin scrolling the thumb.
     * The system means if the user scrolls up then the y value will set to 1 not increase!
     * The same with the down direction.
     */
    public static class ScrollCallback extends Callback<GLFWScrollCallbackI> implements GLFWScrollCallbackI {
        @Override
        public void invoke(long window, double xOffset, double yOffset) {
            for(GLFWScrollCallbackI callback : getCallbacks()) {
                callback.invoke(window, xOffset, yOffset);
            }
        }
    }

}
