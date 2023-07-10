package net.exsource.openui;

import net.exsource.openlogger.Logger;
import net.exsource.openui.ui.UIWindow;
import net.exsource.openui.ui.frame.Window;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class controls the general ui system. This is needed
 * for registering {@link Thread}'s or {@link UIWindow}'s. If you create
 * a window by your own you should save this in a list to get it again.
 * Note that this system is generally used for our library, to use this correct we
 * need to create windows like {@link #createWindow(String, String, int, int, Class)}.
 * This will create a new {@link UIWindow} with the specified type like {@link Window} class.
 * For more information visit our website: <a href="https://www.exsource.de">click here</a>.
 * @since 1.0.0
 * @see UIWindow
 * @see Thread
 * @see Window
 * @author Daniel Ramke
 */
public final class UIFactory {

    private static final Logger logger = Logger.getLogger();

    private static final Map<String, UIWindow> windows = new HashMap<>();
    private static final Map<String, Thread> threads = new HashMap<>();

    /* ########################################################################
     *
     *                             Window/Handle
     *
     * ######################################################################## */

    /**
     * Function called {@link #createWindow(String, String, int, int, Class)} as parent function.
     * @param type the window specified type. Need to inherit {@link UIWindow}.
     * @return T - the create window as correct java object type.
     * @param <T> override the function return value to your class type.
     * @see UIWindow
     */
    public static <T extends UIWindow> T createWindow(Class<T> type) {
        return createWindow(null, null, UIWindow.DEFAULT_WIDTH, UIWindow.DEFAULT_HEIGHT, type);
    }

    /**
     * Function called {@link #createWindow(String, String, int, int, Class)} as parent function.
     * @param title the title of the window, can be null will be replaced by {@link Class#getSimpleName()}.
     * @param type the window specified type. Need to inherit {@link UIWindow}.
     * @return T - the create window as correct java object type.
     * @param <T> override the function return value to your class type.
     * @see UIWindow
     */
    public static <T extends UIWindow> T createWindow(String title, Class<T> type) {
        return createWindow(null, title, UIWindow.DEFAULT_WIDTH, UIWindow.DEFAULT_HEIGHT, type);
    }

    /**
     * Function called {@link #createWindow(String, String, int, int, Class)} as parent function.
     * @param width the window frame width.
     * @param height the window frame height.
     * @param type the window specified type. Need to inherit {@link UIWindow}.
     * @return T - the create window as correct java object type.
     * @param <T> override the function return value to your class type.
     * @see UIWindow
     */
    public static <T extends UIWindow> T createWindow(int width, int height, Class<T> type) {
        return createWindow(null, null, width, height, type);
    }

    /**
     * Function called {@link #createWindow(String, String, int, int, Class)} as parent function.
     * @param title the title of the window, can be null will be replaced by {@link Class#getSimpleName()}.
     * @param width the window frame width.
     * @param height the window frame height.
     * @param type the window specified type. Need to inherit {@link UIWindow}.
     * @return T - the create window as correct java object type.
     * @param <T> override the function return value to your class type.
     * @see UIWindow
     */
    public static <T extends UIWindow> T createWindow(String title, int width, int height, Class<T> type) {
        return createWindow(null, title, width, height, type);
    }

    /**
     * Function creates a new window by specified credentials.
     * It is recommended to use this function to create windows because it will register and handle
     * your window. The created window will return as thy type your give as parameter. This means
     * if your type for example {@link Window} than is the return type this too.
     * <p>
     * @param ID the named identifier can be null to auto generate.
     * @param title the title of the window, can be null will be replaced by {@link Class#getSimpleName()}.
     * @param width the window frame width.
     * @param height the window frame height.
     * @param type the window specified type. Need to inherit {@link UIWindow}.
     * @return T - the create window as correct java object type.
     * @param <T> override the function return value to your class type.
     * @see UIWindow
     */
    @SuppressWarnings("unchecked")
    public static <T extends UIWindow> T createWindow(String ID, String title, int width, int height, Class<T> type) {
        T window;

        if(type != null) {
            try {
                window = (T) Class.forName(type.getName()).getDeclaredConstructor(String.class).newInstance(ID);
                logger.debug("Using " + type.getSimpleName() + ", as window type!");
            } catch (Exception exception) {
                logger.error(exception);
                window = (T) new Window(ID);
            }
        } else {
            logger.warn("Class type was null, we used fallback window!");
            window = (T) new Window(ID);
        }

        window.setTitle(title);
        window.setWidth(width);
        window.setHeight(height);
        registerWindow(window);
        logger.debug("Created window " + window.getIdentifier() + ", successfully!");
        return window;
    }

    /**
     * Function added new windows and check if there exist.
     * This can be used to find your window anywhere you need it.
     * You can get your window by {@link #getWindow(String)} or as parameter the class
     * object itself.
     * Note that this function is called at the {@link #} function and his
     * overloaded functions.
     * @param window the window which needed registering.
     * @see UIWindow
     */
    public static void registerWindow(@NotNull UIWindow window) {
        if(containsWindow(window)) {
            logger.warn("There is a window with the same name!");
            return;
        }

        windows.put(window.getIdentifier(), window);
        logger.debug("Added new window - " + window.getIdentifier());
    }

    /**
     * Function used {@link #unregisterWindow(String)} to work.
     * @param window will use as identifier.
     * @see UIWindow
     */
    public static void unregisterWindow(@NotNull UIWindow window) {
        unregisterWindow(window.getIdentifier());
    }

    /**
     * Function unregistered a window and called its {@link UIWindow#destroy()} function.
     * If you doesn't need this window anymore than call this function!
     * @param ID the window identifier.
     * @see UIWindow
     */
    public static void unregisterWindow(@NotNull String ID) {
        if(!containsWindow(ID)) {
            logger.warn("There is no window with the identifier " + ID);
            return;
        }

        windows.remove(ID);
        logger.debug("Removed window - " + ID);
    }

    /**
     * Function generates a list object from the {@link #windows} map.
     * @return List<AbstractWindow> - a list of all known windows.
     */
    public static List<UIWindow> getWindowList() {
        List<UIWindow> windowList = new ArrayList<>();
        for(Map.Entry<String, UIWindow> entry : windows.entrySet()) {
            windowList.add(entry.getValue());
        }
        return windowList;
    }

    /**
     * Function checks if a window already exist or not.
     * @param window window to check.
     * @return boolean - check state true means it was found.
     */
    public static boolean containsWindow(@NotNull UIWindow window) {
        return getWindow(window) != null;
    }

    /**
     * Function checks if a window already exist or not.
     * @param ID window get by id to check.
     * @return boolean - check state true means it was found.
     */
    public static boolean containsWindow(@NotNull String ID) {
        return getWindow(ID) != null;
    }

    /**
     * Function search for a window and will return it.
     * @param window to get the identifier for searching.
     * @return AbstractWindow - founded window can be null.
     */
    public static UIWindow getWindow(@NotNull UIWindow window) {
        return getWindow(window.getIdentifier());
    }

    /**
     * Function search for a window and will return it.
     * @param ID to searched window identifier.
     * @return AbstractWindow - founded window can be null.
     */
    public static UIWindow getWindow(@NotNull String ID) {
        return windows.get(ID);
    }

    /**
     * @return Map<String, AbstractWindow> - the generated map with all the windows we used.
     */
    public static Map<String, UIWindow> getWindows() {
        return windows;
    }

    /* ########################################################################
     *
     *                             Thread/Handle
     *
     * ######################################################################## */

    /**
     * Function generated a thread which is used by the given window.
     * This is helpful to bind the thread to the window. Please use this
     * function instance of create the thread in the window class, because it is
     * not recommended to do that.
     * @param run the runnable function, in the most cases run().
     * @param window the window which will be stored this thread.
     * @return Thread - a java thread for separate working.
     * @see UIWindow
     * @see Thread
     */
    public static Thread generateThread(@NotNull Runnable run, @NotNull UIWindow window) {
        Thread thread = null;
        if(hasThread(window)) {
            logger.warn("Window " + window.getIdentifier() + ", already have an thread!");
            return window.getThread();
        }

        if(UIProperties.getThreadLimit() < -1 || UIProperties.getThreadLimit() > activeThreads()) {
            thread = new Thread(run, window.getIdentifier());
            threads.put(window.getIdentifier(), thread);
            logger.debug("Generate thread for " + window.getIdentifier());
        } else {
            logger.warn("Max Thread limit is reached! Remove windows or create sub windows for more...");
        }
        return thread;
    }

    /**
     * @param window which is checked.
     * @return boolean - true if the window have a thread.
     */
    public static boolean hasThread(@NotNull UIWindow window) {
        return hasThread(window.getIdentifier());
    }

    /**
     * @param ID identifier for window which is checked.
     * @return boolean - true if the window have a thread.
     */
    public static boolean hasThread(@NotNull String ID) {
        return threads.containsKey(ID);
    }

    /**
     * @param window to get identifier.
     * @return Thread - current window {@link Thread} can be null if not a main window.
     */
    public static Thread getThread(@NotNull UIWindow window) {
        return getThread(window.getIdentifier());
    }

    /**
     * @param ID window identifier.
     * @return Thread - current window {@link Thread} can be null if not a main window.
     */
    public static Thread getThread(@NotNull String ID) {
        return threads.get(ID);
    }

    /**
     * @return int - threads map {@link Map#size()} function.
     */
    public static int activeThreads() {
        return threads.size();
    }

    /**
     * @return Map<String, Thread> - the current holden and using threads.
     * @see Thread
     */
    public static Map<String, Thread> getThreads() {
        return threads;
    }
}
