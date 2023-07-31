package net.exsource.open;

import net.exsource.open.ui.UIWindow;
import net.exsource.open.ui.font.Font;
import net.exsource.open.ui.modals.Image;
import net.exsource.open.ui.windows.Window;
import net.exsource.openlogger.Logger;
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
@SuppressWarnings("unused")
public final class UIFactory {

    private static final Logger logger = Logger.getLogger();

    private static final Map<String, UIWindow> windows = new HashMap<>();
    private static final Map<String, Thread> threads = new HashMap<>();

    private static final List<Font> fonts = new ArrayList<>();
    private static final List<Image> images = new ArrayList<>();

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

        if(activeThreads() < OpenUI.getOptions().getMaxThreads()) {
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

    /* ########################################################################
     *
     *                             Assets/Handle
     *
     * ######################################################################## */

    /* ##################################
     *              Fonts
     * ################################## */

    /**
     * Function registered a new {@link Font} by the constructor call.
     * This is needed to save memory of the users pc.
     * @param font the font object.
     * @see Font
     */
    public static void registerFont(@NotNull Font font) {
        if(hasFont(font)) {
            logger.warn("Font " + font.getName() + ", already loaded!");
            return;
        }
        logger.debug("Font " + font.getName() + ", successfully registered!");
        fonts.add(font);
    }

    /**
     * Unregistered all existing {@link Font}'s in the font {@link List}.
     * Make this after closing the {@link OpenUI} application.
     */
    public static void unregisterAllFonts() {
        fonts.clear();
    }

    /**
     * Function called {@link #unregisterFont(String)} to unregister the given {@link Font}.
     * @param font the font object.
     */
    public static void unregisterFont(@NotNull Font font) {
        unregisterFont(font.getName());
    }

    /**
     * Function to handle unregistering for {@link Font}'s. This is needed
     * if we don't need the {@link Font} anymore. Please don't unregister the
     * {@link Font#FALLBACK} font this can brok you application!
     * @param name the name of the font (folder name).
     */
    public static void unregisterFont(@NotNull String name) {
        if(!hasFont(name)) {
            logger.warn("Font " + name + ", is not loaded or registered!");
            return;
        }
        logger.debug("Font " + name + ", successfully unregistered!");
        fonts.remove(getFont(name));
    }

    /**
     * Function checks if the given font registered or not.
     * @param font the font object.
     * @return {@link Boolean} - true if the font was found.
     */
    public static boolean hasFont(@NotNull Font font) {
        return hasFont(font.getName());
    }

    /**
     * Function checks if the given font registered or not.
     * @param name the font name.
     * @return {@link Boolean} - true if the font was found.
     */
    public static boolean hasFont(@NotNull String name) {
        return getFont(name) != null;
    }

    /**
     * Function searched for a {@link Font} in the fonts {@link List} by the given name.
     * @param name the font name.
     * @return {@link Font} - null if the {@link Font} not found.
     */
    public static Font getFont(@NotNull String name) {
        Font font = getFallbackFont();
        for(Font entries : fonts) {
            if(entries.getName().equals(name)) {
                font = entries;
                break;
            }
        }
        return font;
    }

    /**
     * @return {@link Font} - the {@link Font#FALLBACK} font.
     */
    public static Font getFallbackFont() {
        return Font.FALLBACK;
    }

    /**
     * @return {@link List} - the complete {@link Font} {@link List}.
     */
    public static List<Font> getFonts() {
        return fonts;
    }

    /* ##################################
     *              Image
     * ################################## */

    public static void registerImage(@NotNull Image image) {
        if(hasImage(image)) {
            logger.warn("Image " + image.getName() + ", already loaded!");
            return;
        }

        logger.debug("Image " + image.getName() + ", successfully registered!");
        images.add(image);
    }

    public static void unregisterAllImages() {
        images.clear();
    }

    public static void unregisterImage(@NotNull Image image) {
        unregisterImage(image.getName());
    }

    public static void unregisterImage(@NotNull String name) {
        if(!hasImage(name)) {
            logger.warn("Image " + name + ", not loaded!");
            return;
        }

        logger.debug("Image " + name + ", successfully unregistered!");
        images.remove(getImage(name));
    }

    public static boolean hasImage(@NotNull Image image) {
        return hasImage(image.getName());
    }

    public static boolean hasImage(@NotNull String name) {
        return getImage(name) != null;
    }

    public static Image getImage(@NotNull String name) {
        Image image = getFallbackImage();
        for(Image entries : images) {
            if(entries.getName().equals(name)) {
                image = entries;
                break;
            }
        }
        return image;
    }

    public static Image getFallbackImage() {
        return Image.FALLBACK;
    }

    public static List<Image> getImages() {
        return images;
    }
}
