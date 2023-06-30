package net.exsource.openui.ui;

import net.exsource.openlogger.Logger;
import net.exsource.openui.UIManager;
import net.exsource.openui.events.windows.WindowDestroyEvent;
import net.exsource.openui.exception.windows.WindowNotThreadedException;
import net.exsource.openutils.event.EventManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Todo: make a fps system with 30, 60, 90, 120, 144, 165, 240, or custom FPS updates. (Only GameWindow).
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

    @Override
    public void run() {
        if(thread == null) {
            logger.error(new WindowNotThreadedException("No Thread found for parent window=" + getSerialID()));
            return;
        }
        build();
        defaultConfigure();
        initialization();
        while (!willClose() && used) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            calculateViewPort();
            if(!isIconified()) {
                NanoVG.nvgBeginFrame(getContext().getNvgID(), getWidth(), getHeight(), 1f);

                update();

                NanoVG.nvgRestore(getContext().getNvgID());
                NanoVG.nvgEndFrame(getContext().getNvgID());
            }
            GLFW.glfwSwapBuffers(getHardwareID());
            GLFW.glfwPollEvents();
        }
        clear();
    }

    @Override
    public void destroy() {
        EventManager.callEvent(new WindowDestroyEvent(this));
        this.used = false;
        clear();
    }

    @Override
    public boolean isParent() {
        return !getChildWindows().isEmpty();
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
