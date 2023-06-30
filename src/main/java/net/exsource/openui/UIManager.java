package net.exsource.openui;

import net.exsource.openlogger.Logger;
import net.exsource.openui.enums.AvailableArguments;
import net.exsource.openui.ui.AbstractWindow;
import net.exsource.openui.ui.ParentWindow;
import net.exsource.openui.ui.windows.DialogWindow;
import net.exsource.openui.ui.windows.Window;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class UIManager {

    private static final Logger logger = Logger.getLogger();

    private static final Map<String, AbstractWindow> registeredWindows = new HashMap<>();
    private static final Map<String, Thread> threadsMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends AbstractWindow> T createWindow(String ID, String title, int width, int height, Class<T> type) {
        T window;
        if(type == null) {
            logger.error(new NullPointerException("The windowType can't be null!"));
            return (T) new Window(ID);
        }
        switch (type.getSimpleName()) {
            case "Window" -> {
                window = (T) new Window(ID);
            }
            case "DialogWindow" -> {
                window = (T) new DialogWindow(ID);
            }
            default -> {
                try {
                    window = (T) Class.forName(type.getName()).getDeclaredConstructor(String.class).newInstance(ID);
                    logger.debug("Using custom windowType=" + type.getSimpleName());
                } catch (Exception exception) {
                    logger.error(exception);
                    logger.warn("Not supported windowType=" + type.getSimpleName() + ", use Fallback Window.class!");
                    window = (T) new Window(ID);
                }
            }
        }
        window.setTitle(title);
        window.setWidth(width);
        window.setHeight(height);
        window.setState(AbstractWindow.State.INITIALIZATION);
        registerWindow(window);
        return window;
    }

    public static <T extends AbstractWindow> T createWindow(String ID, int width, int height, Class<T> type) {
        return createWindow(ID, null, width, height, type);
    }

    public static <T extends AbstractWindow> T createWindow(String ID, String title, Class<T> type) {
        return createWindow(ID, title, AbstractWindow.DEFAULT_WIDTH, AbstractWindow.DEFAULT_HEIGHT, type);
    }

    public static <T extends AbstractWindow> T createWindow(String title, Class<T> type) {
        return createWindow(null, title, AbstractWindow.DEFAULT_WIDTH, AbstractWindow.DEFAULT_HEIGHT, type);
    }

    public static <T extends AbstractWindow> T createWindow(Class<T> type) {
        return createWindow(null, null, AbstractWindow.DEFAULT_WIDTH, AbstractWindow.DEFAULT_HEIGHT, type);
    }

    public static void registerWindow(AbstractWindow window) {
        if(window == null) {
            logger.error(new NullPointerException("Can't register a null window!"));
            return;
        }
        if(containsWindow(window.getSerialID())) {
            logger.warn("Can't register a window with the same serailID between other windows!");
            return;
        }
        registeredWindows.put(window.getSerialID(), window);
        logger.debug("Added new window=" + window.getSerialID());
    }

    public static void unregisterWindow(@NotNull AbstractWindow window) {
        unregisterWindow(window.getSerialID());
    }

    public static void unregisterWindow(@NotNull String ID) {
        if(!containsWindow(ID)) {
            logger.warn("Can't find a registered window with ID=" + ID);
            return;
        }
        getWindow(ID).destroy();
        threadsMap.remove(ID);
        registeredWindows.remove(ID);

        logger.debug("Destroy and remove window with ID=" + ID + ", was successfully!");
    }

    public static void cleanup() {
        registeredWindows.forEach((ID, window) -> {
            window.destroy();
            threadsMap.remove(ID);
        });
        registeredWindows.clear();
    }

    public static AbstractWindow getWindow(@NotNull String ID) {
        AbstractWindow window = null;
        for(String windowIDs : registeredWindows.keySet()) {
            if(windowIDs.equals(ID)) {
                window = registeredWindows.get(ID);
                break;
            }
        }
        return window;
    }

    public static Thread generateWindowThread(@NotNull AbstractWindow window) {
        Thread thread = null;
        if(OpenUI.getProperties().hasKey(AvailableArguments.UI_MAX_THREADS.getParam())) {
            int value = OpenUI.getProperties().getValue(AvailableArguments.UI_MAX_THREADS.getParam(), int.class);
            if(value > threadsMap.size()) {
                if(window instanceof ParentWindow) {
                    thread = new Thread(window::run, window.getSerialID());
                    threadsMap.put(window.getSerialID(), thread);
                } else {
                    logger.warn("Window " + window.getSerialID() + " is not marked as parent window!");
                    logger.warn("Only parent windows can create and used threads!");
                }
            }
        } else {
            //Todo: check at threads available currently.
            if(window instanceof ParentWindow) {
                thread = new Thread(window::run, window.getSerialID());
                threadsMap.put(window.getSerialID(), thread);
            }
        }
        return thread;
    }

    public static boolean containsWindow(@NotNull String ID) {
        return getWindow(ID) != null;
    }

    public static Map<String, AbstractWindow> getRegisteredWindows() {
        return registeredWindows;
    }
}
