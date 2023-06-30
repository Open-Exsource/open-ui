package net.exsource.openui.ui;

import net.exsource.openlogger.Logger;
import net.exsource.openui.UIManager;
import net.exsource.openui.exception.WindowNotThreadedException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Todo: make a fps system with 30, 60, 90, 120, 144, 165, 240, or custom FPS updates.
public abstract class ParentWindow extends AbstractWindow {

    private static final Logger logger = Logger.getLogger();

    private final Map<String, ChildWindow> childWindowMap = new HashMap<>();

    private final Thread thread;

    public ParentWindow(String serialID) {
        super(serialID);

        this.thread = UIManager.generateWindowThread(this);
        if(thread != null) {
            thread.start();
            this.used = true;
        }
    }

    protected abstract void initialization();
    protected abstract void update();
    protected abstract void clear();

    //Todo: controls and include opengl stuff. Look for the Performance!
    @Override
    public void run() {
        if(thread == null) {
            logger.error(new WindowNotThreadedException("No Thread found for parent window=" + getSerialID()));
            return;
        }
        initialization();
        for(ChildWindow window : getChildWindows()) {
            window.initialization();
        }
        while (used) {
            update();
            for(ChildWindow window : getChildWindows()) {
                window.update();
            }
        }
        for(ChildWindow window : getChildWindows()) {
            window.clear();
        }
        clear();
    }

    @Override
    public void destroy() {
        this.used = false;
        clear();
    }

    @Override
    public boolean isParent() {
        return true;
    }

    public Thread getThread() {
        return thread;
    }

    public void addChildWindow(@NotNull ChildWindow window) {
        if(containsChildWindow(window)) {
            logger.warn("Window " + getSerialID() + ", contains child window " + window.getSerialID() + " already!");
            return;
        }
        childWindowMap.put(window.getSerialID(), window);
        window.setParent(this);
    }

    public void removeChildWindow(@NotNull ChildWindow window) {
        removeChildWindow(window.getSerialID());
    }

    public void removeChildWindow(@NotNull String ID) {
        if(!containsChildWindow(ID)) {
            logger.warn("Window " + getSerialID() + ", dosen't contains child window " + ID);
            return;
        }
        getChildWindow(ID).setParent(null);
        childWindowMap.remove(ID);
    }

    public ChildWindow getChildWindow(@NotNull ChildWindow window) {
        return getChildWindow(window.getSerialID());
    }

    public ChildWindow getChildWindow(@NotNull String ID) {
        ChildWindow window = null;
        for(ChildWindow entry : getChildWindows()) {
            if(entry.getSerialID().equals(ID)) {
                window = entry;
                break;
            }
        }
        return window;
    }

    public boolean containsChildWindow(@NotNull String ID) {
        return getChildWindow(ID) != null;
    }

    public boolean containsChildWindow(@NotNull ChildWindow window) {
        return containsChildWindow(window.getSerialID());
    }

    public List<ChildWindow> getChildWindows() {
        List<ChildWindow> windows = new ArrayList<>();
        for(Map.Entry<String, ChildWindow> entry : childWindowMap.entrySet()) {
            windows.add(entry.getValue());
        }
        return windows;
    }

    public Map<String, ChildWindow> getChildWindowMap() {
        return childWindowMap;
    }
}
