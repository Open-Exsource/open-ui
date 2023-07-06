package net.exsource.openui.logic;

import net.exsource.openlogger.Logger;
import net.exsource.openui.events.renderer.RendererInitializeEvent;
import net.exsource.openui.ui.UIWindow;
import net.exsource.openutils.event.EventManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRenderer implements Renderer {

    private static final Logger logger = Logger.getLogger();

    private static final List<String> registeredNames = new ArrayList<>();
    private final String name;

    private RenderPriority priority;
    private UIWindow window;

    private boolean initialized;
    private boolean needPatch;

    public AbstractRenderer(String name) {
        this.name = checkName(name);
        this.priority = RenderPriority.MODERATE;
        this.initialized = false;
        this.needPatch = false;
    }

    protected abstract void load(UIWindow window);
    protected abstract void func(UIWindow window);

    @Override
    public void initialize() {
        if(initialized) {
            return;
        }
        initialized = true;
        EventManager.callEvent(new RendererInitializeEvent(this));
        logger.debug("Renderer " + getName() + " initialized!");
    }

    @Override
    public void render(UIWindow window) {
        if(window == null) {
            return;
        }

        if(this.window == null) {
            this.window = window;
            load(window);
        }

        func(window);
    }

    @Override
    public void dispose() {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPriority(RenderPriority priority) {
        this.priority = priority;
        this.setNeededPatch(true);
    }

    @Override
    public RenderPriority getPriority() {
        return priority;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setNeededPatch(boolean need) {
        this.needPatch = need;
    }

    @Override
    public boolean neededPatch() {
        return false;
    }

    public UIWindow getWindow() {
        return window;
    }

    private String checkName(String name) {
        if(name == null) {
            name = getClass().getSimpleName();
        }

        if(existName(name)) {
            int index = 0;
            for (String names : registeredNames) {
                if(names.startsWith(names) || names.startsWith(names + "-")) {
                    index++;
                }
            }
            name = name + "-" + index;
        }

        registeredNames.add(name);
        return name;
    }

    private boolean existName(@NotNull String name) {
        boolean state = false;
        for(String names : registeredNames) {
            if(name.equals(names)) {
                state = true;
                break;
            }
        }
        return state;
    }

    public static List<String> getRegisteredNames() {
        return registeredNames;
    }
}
