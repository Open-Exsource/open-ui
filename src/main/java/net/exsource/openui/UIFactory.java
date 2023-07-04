package net.exsource.openui;

import net.exsource.openlogger.Logger;
import net.exsource.openui.ui.AbstractWindow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UIFactory {

    private static final Logger logger = Logger.getLogger();

    private static final Map<String, AbstractWindow> windows = new HashMap<>();

    public static List<AbstractWindow> getWindowList() {
        List<AbstractWindow> windowList = new ArrayList<>();
        for(Map.Entry<String, AbstractWindow> entry : windows.entrySet()) {
            windowList.add(entry.getValue());
        }
        return windowList;
    }

    public static boolean containsWindow(@NotNull AbstractWindow window) {
        return getWindow(window) != null;
    }

    public static boolean containsWindow(@NotNull String ID) {
        return getWindow(ID) != null;
    }

    public static AbstractWindow getWindow(@NotNull AbstractWindow window) {
        return getWindow(window.getIdentifier());
    }

    public static AbstractWindow getWindow(@NotNull String ID) {
        return windows.get(ID);
    }

    public static Map<String, AbstractWindow> getWindows() {
        return windows;
    }

    public static Thread generateThread(@NotNull Runnable run, @NotNull AbstractWindow window) {
        return new Thread(run, window.getIdentifier()); //Todo: save this and make security checks...
    }

}
