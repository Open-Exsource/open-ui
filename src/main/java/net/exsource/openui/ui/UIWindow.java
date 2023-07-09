package net.exsource.openui.ui;

import net.exsource.openlogger.Logger;
import net.exsource.openlogger.level.LogLevel;
import net.exsource.openlogger.util.ConsoleColor;
import net.exsource.openui.OpenUI;
import net.exsource.openui.UIFactory;
import net.exsource.openui.enums.Errors;
import net.exsource.openui.events.windows.WindowCloseEvent;
import net.exsource.openui.events.windows.WindowCreateEvent;
import net.exsource.openui.exception.windows.WindowCantBuildException;
import net.exsource.openui.logic.AbstractRenderer;
import net.exsource.openui.logic.Renderer;
import net.exsource.openui.logic.input.Keyboard;
import net.exsource.openui.logic.input.Mouse;
import net.exsource.openui.logic.renderer.UIBackgroundRenderer;
import net.exsource.openui.ui.component.Component;
import net.exsource.openui.ui.frame.Window;
import net.exsource.openutils.event.EventManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.lwjgl.system.MemoryUtil.NULL;
import static net.exsource.openui.logic.callback.Callbacks.*;

/**
 * This class is used to create new windows like {@link AbstractWindow}. You choose this class
 * if you need to write your own frame system for game loops or something. If you wish
 * to use our format then use the {@link AbstractWindow} class instance of this. Because the {@link AbstractWindow}
 * class contains an own frame system which allows to change the frame rates. Note that this class
 * doesn't contain a while loop for rendering or update the window. You should to create it by ur self.
 * If you need help by this then look at the official site of exsource.de or lwjgl.
 * It is recommended to use our {@link AbstractWindow} class for create own windows look at
 * {@link Window} for the work usage.
 * @since 1.0.0
 * @see CountDownLatch
 * @see Thread
 * @author Daniel Ramke
 */
public abstract class UIWindow {

    private static final Logger logger = Logger.getLogger();

    public static final Integer DEFAULT_WIDTH = 800;
    public static final Integer DEFAULT_HEIGHT = 600;

    private final CountDownLatch latch;
    private final Thread thread;

    private final String identifier;
    private long openglID;

    protected Context context;

    private String title;

    private int width;
    private int height;

    private boolean created;
    private boolean vsync;

    private final List<Renderer> renderers = new ArrayList<>();
    private final List<Component> components = new ArrayList<>();

    private WindowRefreshCallback refreshCallback;
    private WindowSizeCallback sizeCallback;
    private WindowCloseCallback closeCallback;
    private WindowPositionCallback positionCallback;
    private WindowFocusCallback focusCallback;
    private WindowIconifyCallback iconifyCallback;
    private WindowMaximizedCallback maximizedCallback;
    private FrameBufferSizeCallback frameBufferSizeCallback;

    private KeyCallback keyCallback;
    private CharCallback charCallback;
    private CharModsCallback charModsCallback;

    private MouseButtonCallback mouseButtonCallback;
    private MouseEnteredCallback mouseEnteredCallback;
    private MousePositionCallback mousePositionCallback;
    private ScrollCallback scrollCallback;

    /* ########################################################################
     *
     *                             Constructors
     *
     * ######################################################################## */

    /**
     * This constructor is use his main constructor. Chose this for fast
     * initialization.
     */
    public UIWindow() {
        this(null);
    }

    /**
     * The main constructor of this class, this will be called
     * if you create a new window which is an instance of this.
     * it is recommended to use this class as window creation helper because you can then use
     * {@link UIFactory#createWindow(String, String, int, int, Class)} to initialize you window.
     * If you need a complete finished loop system which can swap fps and ups then use
     * {@link AbstractWindow} instance of {@link UIWindow}.
     * @param identifier the identifier for the window. If it null then it will create an identifier by
     *                   {@link #generateSerialID(String)}.
     * @see AbstractWindow
     * @see UIFactory
     */
    public UIWindow(String identifier) {
        this.latch = new CountDownLatch(1);
        this.identifier = generateSerialID(identifier);
        this.title = getClass().getSimpleName();
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.vsync = false;
        this.thread = UIFactory.generateThread(this::run, this);
        thread.start();
    }

    /* ########################################################################
     *
     *                       Implementation / abstraction
     *
     * ######################################################################## */

    /**
     * Function is called in constructor as thread runnable. The function will be
     * used as parameter for {@link UIFactory#generateThread(Runnable, UIWindow)}
     * its replaced the run param. We call here the abstract functions {@link #initialize()},
     * {@link #loop()} and {@link #destroy()}. You can use this three functions to control the life
     * span of the window.
     * @see AbstractWindow
     * @see UIFactory
     */
    protected void run() {
        initialize();
        loop();
        destroy();
    }

    /**
     * Function is used for the initialization state at a window.
     * You can create here object like one use thinks or variables like final. Note I mean like final you can't
     * use here final as key word because this can only in constructors. This function is called automatically
     * in the {@link #run()} life span.
     */
    protected abstract void initialize();

    /**
     * Function is used for the update/looping state for a window.
     * You can create here object like multi use thinks. Note don't use this function for initialization thinks
     * because it will call all milliseconds. If you need an object which need one time initialization then
     * use the {@link #initialize()} function instance of this.
     * This function is called automatically
     * in the {@link #run()} life span.
     */
    protected abstract void loop();

    /**
     * Function is used for clean up your window. Use this after the window is closed for
     * clean list, maps or buffers. This was an example, you can here make what you want.
     * This is the end of a window life span and not the best location to create or initialize thinks.
     */
    public abstract void destroy();

    /* ########################################################################
     *
     *                           General Getter/Setter
     *
     * ######################################################################## */

    /**
     * @return String - window named identifier, a readable name for humans.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return long - from glfw created id for the window, is better for the machine to read.
     */
    public long getOpenglID() {
        _wait();
        return openglID;
    }

    /**
     * @return Context - the context archive contains openglID, nvgID and GLCapabilities.
     */
    public Context getContext() {
        return context;
    }

    /**
     * @return Thread - the java thread which is holding the current window.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * @return String - window type is {@link Class#getSimpleName()} because the name of class ar the type.
     */
    public String getType() {
        return getClass().getSimpleName();
    }

    //ToDo: include better function.
    public void setTitle(String title) {
        if(title == null) {
            title = getClass().getSimpleName();
        }
        this.title = title;
        GLFW.glfwSetWindowTitle(getOpenglID(), title);
    }

    /**
     * @return String - the current using title from the window.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return boolean - true if the window was created by {@link #build()}.
     */
    public boolean isCreated() {
        _wait();
        return created;
    }

    /**
     * Function change vsync state for window.
     * @param state the new vsync state.
     */
    public void setVsync(boolean state) {
        _wait();
        this.vsync = state;
        GLFW.glfwSwapInterval(state ? 1 : 0);
    }

    /**
     * @return boolean - state of window vsync.
     */
    public boolean isVsync() {
        _wait();
        return vsync;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return int - the current window width.
     */
    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return int - the current window height.
     */
    public int getHeight() {
        return height;
    }

    /* ########################################################################
     *
     *                           Default Functions
     *
     * ######################################################################## */

    /**
     * Function generates a new GLFW window by using {@link GLFW#glfwCreateWindow(int, int, ByteBuffer, long, long)}.
     * This will set up the class and usable for the user. You can override this function if you need your own system.
     * But make sure you set the openglID.
     */
    protected void build() {
        logger.info("Build window " + getIdentifier() + "...");
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        this.openglID = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        if(openglID <= NULL) {
            this.created = false;
            logger.fatal(new WindowCantBuildException("Can't build window " + getIdentifier()));
            OpenUI.exit(Errors.WINDOW_BUILD.getCode());
            return;
        }

        loadDefaultCallbacks();
        loadDefaultRenderers();

        GLFW.glfwMakeContextCurrent(openglID);
        GLFW.glfwSwapInterval(vsync ? 1 : 0);
        logger.info("Window HID=" + openglID + ", named=" + getIdentifier());
        this.created = true;
        EventManager.callEvent(new WindowCreateEvent(this));
        latch.countDown();
    }

    /**
     * Function creates the configuration for the created window by {@link #build()}. Make sure you're using
     * this function after {@link #build()} was called. If you swap this to or ignores the build state, the window
     * will not work, because the function needs the openglID.
     */
    protected void defaultConfigure() {
        GLCapabilities capabilities = GL.createCapabilities();
        long nvgID = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_STENCIL_STROKES | NanoVGGL3.NVG_ANTIALIAS);
        this.context = new Context(openglID, nvgID, capabilities);

        logger.debug("Crating context for " + getIdentifier());
        printGraphicCardInformation();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Function set the current viewport of opengl by calling {@link GL11#glViewport(int, int, int, int)}.
     * This is helpful for games which need more flexibility.
     */
    protected void calculateViewPort() {
        GL11.glViewport(0, 0, getWidth(), (int) getHeight());
    }

    /* ########################################################################
     *
     *                               Renderers
     *
     * ######################################################################## */

    /**
     * Function to add renderer to the current window. Choose a render template, {@link Renderer} is an
     * interface and can't be used by itself. Our recommended renderer class is {@link AbstractRenderer} which
     * contains more useful functions.
     * The function will return if the given renderer already exist.
     * @param renderer the renderer to add.
     * @see Renderer
     * @see AbstractRenderer
     */
    public void addRenderer(@NotNull Renderer renderer) {
        if(hasRenderer(renderer)) {
            logger.warn("Renderer " + renderer.getName() + ", is already included at " + getIdentifier());
            return;
        }

        renderers.add(renderer);
        logger.debug("Added new renderer " + renderer.getName() + ", to window " + getIdentifier());
    }

    /**
     * Function removes an existing renderer which is running in the window. Note that
     * the {@link Renderer} will call his {@link Renderer#dispose()} function before he will
     * remove.
     * The function will return if the given renderer not exist.
     * @param renderer the renderer instance itself as identifier.
     * @see Renderer
     */
    public void removeRenderer(@NotNull Renderer renderer) {
        removeRenderer(renderer.getName());
    }

    /**
     * Function removes an existing renderer which is running in the window. Note that
     * the {@link Renderer} will call his {@link Renderer#dispose()} function before he will
     * remove.
     * The function will return if the given renderer not exist.
     * @param name the renderer name as identifier.
     * @see Renderer
     */
    public void removeRenderer(@NotNull String name) {
        if(!hasRenderer(name)) {
            logger.warn("Renderer " + name + ", is not included at " + getIdentifier());
            return;
        }

        Renderer renderer = getRenderer(name);
        renderer.dispose();
        renderers.remove(renderer);
        logger.debug("Removed renderer " + renderer.getName() + ", from window " + getIdentifier());
    }

    /**
     * Function remove all existing renderers in the window.
     * The {@link Renderer} will call {@link Renderer#dispose()} before he is removing.
     * @see Renderer
     */
    public void removeAllRenders() {
        for(Renderer renderer : renderers) {
            renderer.dispose();
        }
        renderers.clear();
        logger.debug("Removed all renderers from window " + getIdentifier());
    }

    /**
     * Function checks if a {@link Renderer} exist in the window or not.
     * @param renderer the renderer as identifier.
     * @return boolean - true if the renderer was found.
     * @see Renderer
     */
    public boolean hasRenderer(@NotNull Renderer renderer) {
        return hasRenderer(renderer.getName());
    }

    /**
     * Function checks if a {@link Renderer} exist in the window or not.
     * @param name the renderer name as identifier.
     * @return boolean - true if the renderer was found.
     * @see Renderer
     */
    public boolean hasRenderer(@NotNull String name) {
        return getRenderer(name) != null;
    }

    /**
     * Function search for a renderer in the {@link #getRenderers()} list with the given
     * renderer parameter. If the renderer was found it will return this renderer.
     * The return value can be null if the renderer wasn't found.
     * @param renderer the renderer as identifier.
     * @return Renderer - the founded renderer object.
     * @see Renderer
     */
    public Renderer getRenderer(@NotNull Renderer renderer) {
        return getRenderer(renderer.getName());
    }

    /**
     * Function search for a renderer in the {@link #getRenderers()} list with the given
     * name parameter. If the renderer was found it will return this renderer.
     * The return value can be null if the renderer wasn't found.
     * @param name the renderer name as identifier.
     * @return Renderer - the founded renderer object.
     * @see Renderer
     */
    public Renderer getRenderer(@NotNull String name) {
        Renderer renderer = null;
        for(Renderer entry : renderers) {
            if(entry.getName().equals(name)) {
                renderer = entry;
            }
        }
        return renderer;
    }

    /**
     * @return List<Renderer> - all known renderers for this window.
     */
    public List<Renderer> getRenderers() {
        return renderers;
    }

    /* ########################################################################
     *
     *                               Components
     *
     * ######################################################################## */

    /**
     * Function will add a new component to the window. Use this for build up ur UI.
     * @param component the component to add.
     * @apiNote Here will come more information if the component system more build.
     * @see Component
     */
    public void addComponent(@NotNull Component component) {
        if(hasComponent(component)) {
            logger.warn("The same component exist with the name " + component.getLocalizedName());
            return;
        }

        components.add(component);
        logger.debug("Added new component " + component.getLocalizedName());
    }

    /**
     * Function removes a specified component form the window by the given {@link Component}.
     * If the component wasn't found
     * at the {@link #getComponents()} list, then it will return and display a warning log.
     * @param component the identifier for find the to delete component.
     * @see Component
     */
    public void removeComponent(@NotNull Component component) {
        removeComponent(component.getLocalizedName());
    }

    /**
     * Function removes a specified component form the window by the given ID.
     * The ID is {@link Component#getLocalizedName()} in this case. If the component wasn't found
     * at the {@link #getComponents()} list, then it will return and display a warning log.
     * @param ID the identifier for find the to delete component.
     * @see Component
     */
    public void removeComponent(@NotNull String ID) {
        if(!hasComponent(ID)) {
            logger.warn("The component " + ID + " doesn't exist in window " + getIdentifier());
            return;
        }

        components.remove(getComponent(ID));
        logger.debug("Removed component " + ID);
    }

    /**
     * Function removes all components from the window. Note that this function is
     * a good usage at the {@link #destroy()} function. Because there we don't need the
     * components anymore.
     */
    public void removeAllComponents() {
        components.clear();
    }

    /**
     * Function checks the {@link #getComponents()} list for a specified {@link Component} by itself as object.
     * @param component the {@link Component} need to be used.
     * @return boolean - true if it was found in the list.
     * @see Component
     */
    public boolean hasComponent(@NotNull Component component) {
        return getComponent(component) != null;
    }

    /**
     * Function checks the {@link #getComponents()} list for a specified {@link Component} by ID.
     * @param ID the {@link Component#getLocalizedName()} need to be used.
     * @return boolean - true if it was found in the list.
     * @see Component
     */
    public boolean hasComponent(@NotNull String ID) {
        return getComponent(ID) != null;
    }

    /**
     * Function searched for a component in the {@link #getComponents()} list by {@link Component} as key parameter.
     * It will call {@link #getComponent(String)} and include {@link Component#getLocalizedName()} as the parameter.
     * Can return null if the component wasn't found.
     * @param component the identifier for the component we ar searching for.
     * @return Component - the object which was found, warning can be null!
     * @see Component
     */
    public Component getComponent(@NotNull Component component) {
        return getComponent(component.getLocalizedName());
    }

    /**
     * Function searched for a component in the {@link #getComponents()} list by ID as key parameter.
     * If the function find a component with the exact same ID it will return it.
     * Can return null if the component wasn't found.
     * @param ID the identifier for the component we ar searching for.
     * @return Component - the object which was found, warning can be null!
     * @see Component
     */
    public Component getComponent(@NotNull String ID) {
        Component component = null;
        for(Component entry : components) {
            if(entry.getLocalizedName().equals(ID)) {
                component = entry;
                break;
            }
        }
        return component;
    }

    /**
     * @return List<Component> - all components which ar used by the window.
     */
    public List<Component> getComponents() {
        return components;
    }

    /* ########################################################################
     *
     *                          GLFW Getter / Setter
     *
     * ######################################################################## */

    /**
     * Function restores the window to default state.
     */
    public void restore() {
        GLFW.glfwRestoreWindow(getOpenglID());
    }

    /**
     * Function triggered the window to close.
     */
    public void close() {
        GLFW.glfwSetWindowShouldClose(getOpenglID(), true);
    }

    /**
     * @return boolean - true if the window was triggered to close.
     */
    public boolean willClose() {
        return GLFW.glfwWindowShouldClose(getOpenglID());
    }

    /**
     * Function will show the window. Is ignored if the window already shown.
     */
    public void show() {
        if(!isVisible()) {
            GLFW.glfwShowWindow(getOpenglID());
        }
    }

    /**
     * Function will hide the window. Is ignored if the window already hidden.
     */
    public void hide() {
        if(isVisible()) {
            GLFW.glfwHideWindow(getOpenglID());
        }
    }

    /**
     * @return boolean - the current visible state, true if is shown.
     */
    public boolean isVisible() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE;
    }

    /**
     * @param maximized true if you wish to maximize.
     */
    public void setMaximized(boolean maximized) {
        if(maximized) {
            GLFW.glfwMaximizeWindow(getOpenglID());
            return;
        }
        this.restore();
    }

    /**
     * @return boolean - current maximize state, false means it is not maximized.
     */
    public boolean isMaximized() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE;
    }

    /**
     * @param iconified true if you wish to iconified the window to the taskbar.
     */
    public void setIconified(boolean iconified) {
        if(iconified) {
            GLFW.glfwIconifyWindow(getOpenglID());
            return;
        }
        this.restore();
    }

    /**
     * @return boolean - current iconified state, false means it is not iconified.
     */
    public boolean isIconified() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE;
    }

    /**
     * @param focused true if you wish that the window is focused now.
     */
    public void setFocused(boolean focused) {
        if(focused) {
            GLFW.glfwFocusWindow(getOpenglID());
            return;
        }
        this.restore();
    }

    /**
     * @return boolean - current focused state, false means it is not focused.
     */
    public boolean isFocused() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
    }

    /**
     * @param alwaysOnTop true if the window need always on top like popups, dialogs for important news or viruses :D.
     */
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        GLFW.glfwSetWindowAttrib(getOpenglID(), GLFW.GLFW_FLOATING, alwaysOnTop ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    /**
     * @return boolean - current always top state, false means it is not on top always.
     */
    public boolean isAlwaysOnTop() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_FLOATING) == GLFW.GLFW_TRUE;
    }

    /**
     * @param resizeable true if the window should be resized.
     */
    public void setResizeable(boolean resizeable) {
        GLFW.glfwSetWindowAttrib(getOpenglID(), GLFW.GLFW_RESIZABLE, resizeable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    /**
     * @return boolean - current resizeable state, false means it is not resizeable.
     */
    public boolean isResizeable() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_RESIZABLE) == GLFW.GLFW_TRUE;
    }

    /* ########################################################################
     *
     *                     Callbacks / Public Functions
     *
     * ######################################################################## */

    /**
     * @return WindowCloseCallback - the callback if the window will close.
     */
    public WindowCloseCallback getCloseCallback() {
        return closeCallback;
    }

    /**
     * @return WindowFocusCallback - the callback if the window will change the focus state.
     */
    public WindowFocusCallback getFocusCallback() {
        return focusCallback;
    }

    /**
     * @return WindowIconifyCallback - the callback if the window change to iconified.
     */
    public WindowIconifyCallback getIconifyCallback() {
        return iconifyCallback;
    }

    /**
     * @return WindowMaximizedCallback - the callback if the window will be changed to maximized or minimized.
     */
    public WindowMaximizedCallback getMaximizedCallback() {
        return maximizedCallback;
    }

    /**
     * @return WindowPositionCallback - the callback if the window position is change. Warning update many times!
     */
    public WindowPositionCallback getPositionCallback() {
        return positionCallback;
    }

    /**
     * @return WindowSizeCallback - the callback if the window size will be change this will call too by {@link #getMaximizedCallback()}.
     */
    public WindowSizeCallback getSizeCallback() {
        return sizeCallback;
    }

    /**
     * @return WindowRefreshCallback - the callback if the window refreshed most call by {@link #restore()}.
     */
    public WindowRefreshCallback getRefreshCallback() {
        return refreshCallback;
    }

    /**
     * @return FrameBufferSizeCallback - same call at {@link #getSizeCallback()}.
     */
    public FrameBufferSizeCallback getFrameBufferSizeCallback() {
        return frameBufferSizeCallback;
    }

    /**
     * @return KeyCallback - the callback if the window detect key inputs.
     */
    public KeyCallback getKeyCallback() {
        return keyCallback;
    }

    /**
     * @return CharCallback - the callback if the window detect key inputs but its give us the right char.
     */
    public CharCallback getCharCallback() {
        return charCallback;
    }

    /**
     * @return CharModsCallback - the callback if the window detect key combos like CTRL + C
     */
    public CharModsCallback getCharModsCallback() {
        return charModsCallback;
    }

    /**
     * @return MouseButtonCallback - the callback if the window detect mouse button inputs.
     */
    public MouseButtonCallback getMouseButtonCallback() {
        return mouseButtonCallback;
    }

    /**
     * @return MousePositionCallback - the callback if the mouse entered the window and is moving.
     */
    public MousePositionCallback getMousePositionCallback() {
        return mousePositionCallback;
    }

    /**
     * @return MouseEnteredCallback - the callback if the mouse entered the window.
     */
    public MouseEnteredCallback getMouseEnteredCallback() {
        return mouseEnteredCallback;
    }

    /**
     * @return ScrollCallback - the callback if the mouse entered the window and is scrolling.
     */
    public ScrollCallback getScrollCallback() {
        return scrollCallback;
    }

    /* ########################################################################
     *
     *                    Callbacks / Private Functions
     *
     * ######################################################################## */

    /**
     * This trigger is called if the window change his current size.
     * @param windowID the window which is resizing.
     * @param width the new width.
     * @param height the new height.
     */
    protected void sizeCallback(long windowID, int width, int height) {

    }

    /**
     * This trigger is called if the window change his current position.
     * @param windowID the window which is reposing.
     * @param positionX the new x position on the desktop.
     * @param positionY the new y position on the desktop.
     */
    protected void positionCallback(long windowID, int positionX, int positionY) {

    }

    /**
     * This trigger is called if the window refreshed by maximize or iconified.
     * @param windowID the window which is refreshed.
     */
    protected void refreshCallback(long windowID) {

    }

    /**
     * This trigger is called before the window is closing.
     * @param windowID the window which will close.
     */
    protected void closeCallback(long windowID) {
        EventManager.callEvent(new WindowCloseEvent(this));
    }

    /**
     * This trigger is called if the window is focused or lost his focus.
     * @param windowID the window which focus.
     * @param focus the current focus state.
     */
    protected void focusCallback(long windowID, boolean focus) {

    }

    /**
     * This trigger is called if the window set to iconified.
     * @param windowID the window which the iconified state.
     * @param iconify the current state.
     */
    protected void iconifyCallback(long windowID, boolean iconify) {

    }

    /**
     * This trigger is called if the window to maximize screen or leave.
     * @param windowID the window which the maximized state.
     * @param maximize the current state
     */
    protected void maximizedCallback(long windowID, boolean maximize) {

    }

    /**
     * This trigger is called if the window change his current size inner box.
     * @param windowID the window which is resizing.
     * @param width the new width.
     * @param height the new height.
     */
    protected void frameBufferSizeCallback(long windowID, int width, int height) {

    }

    /**
     * This trigger is called if the window detected keyboard input.
     * @param windowID the window which detect key inputs.
     * @param key the key as integer (char)
     * @param scancode the final key id.
     * @param action the current action
     *               <ul>
     *               <li><span style="color: #1ac7b0">GLFW_PRESS</span> this means you only tipped the key.</li>
     *               <li><span style="color: #1ac7b0">GLFW_REPEAT</span> this means you hold the key pressed.</li>
     *               <li><span style="color: #1ac7b0">GLFW_RELEASE</span> this means you released the key.</li>
     *               </ul>
     * @param mods the hotkey which is used example: CTRL + A = 2.
     */
    protected void keyCallback(long windowID, int key, int scancode, int action, int mods) {

    }

    /**
     * This trigger is called if the window detect key input if is a char.
     * @param windowID the window which detect the key input.
     * @param codepoint the char which was tipped.
     */
    protected void charCallback(long windowID, int codepoint) {

    }

    /**
     * This trigger is called if the window detect key input if is a char type or mod type.
     * @param windowID the window which detect the key input.
     * @param codepoint the char which was tipped.
     * @param mods the current mod like SHIFT + a = A | 1.
     */
    protected void charModsCallback(long windowID, int codepoint, int mods) {

    }

    /**
     * This trigger is called if the window detect mouse inputs.
     * @param windowID the window which detect mouse inputs.
     * @param button the pressed and released button.
     * @param action the current action
     *               <ul>
     *               <li><span style="color: #1ac7b0">GLFW_PRESS</span> this means you only tipped the key.</li>
     *               <li><span style="color: #1ac7b0">GLFW_RELEASE</span> this means you released the key.</li>
     *               </ul>
     * @param mods the hotkey which is used example: CTRL + Button0 = 2.
     */
    protected void mouseButtonCallback(long windowID, int button, int action, int mods) {

    }

    /**
     * This trigger is called if the mouse entered the window.
     * @param windowID the window which detect mouse movement.
     * @param positionX the current mouse x position on the window screen.
     * @param positionY the current mouse y position on the window screen.
     */
    protected void mousePositionCallback(long windowID, double positionX, double positionY) {

    }

    /**
     * This trigger is called if the mouse entered and leaved the window border.
     * @param windowID the window which detect the entered and the leaving.
     * @param entered the state of the mouse entered.
     */
    protected void mouseEnteredCallback(long windowID, boolean entered) {

    }

    /**
     * This trigger is called if the mouse on the window and scroll.
     * @param windowID the window which detect the scroll from the entered mouse.
     * @param xOffset the x offset of the scroll.
     * @param yOffset the y offset of the scroll.
     */
    protected void scrollCallback(long windowID, double xOffset, double yOffset) {

    }

    /* ########################################################################
     *
     *                             Private / Misc
     *
     * ######################################################################## */

    /**
     * Function to cast the current class to your window type. This is simple
     * if you write a line with <code>Window window = new Window();</code> you can now use this like
     * <code>Window window = UIFactory.getWindow("Test").casted();</code>.
     * @return T - the current window as his class.
     * @param <T> change type by T construct.
     */
    @SuppressWarnings("unchecked")
    public <T> T casted() {
        T window = (T) this;
        try {
            window = (T) Class.forName(getClass().getName());
        } catch (Exception exception) {
            logger.error(exception);
        }
        return window;
    }

    /**
     * Private function to register the default renders.
     * Default renderers include all {@link AbstractRenderer} as parent class.
     * The most of registered renderers here start with UI in the name.
     * @see AbstractRenderer
     */
    private void loadDefaultRenderers() {
        renderers.add(new UIBackgroundRenderer());
    }

    /**
     * Private function to load all existing callbacks for the window.
     * This is absolut needed for interact with the current {@link UIWindow}.
     * If you let this out it will doesn't work anymore!
     */
    private void loadDefaultCallbacks() {
        sizeCallback = new WindowSizeCallback();
        sizeCallback.add(GLFW.glfwSetWindowSizeCallback(openglID, sizeCallback));
        sizeCallback.add(this::sizeCallback);

        positionCallback = new WindowPositionCallback();
        positionCallback.add(GLFW.glfwSetWindowPosCallback(openglID, positionCallback));
        positionCallback.add(this::positionCallback);

        closeCallback = new WindowCloseCallback();
        closeCallback.add(GLFW.glfwSetWindowCloseCallback(openglID, closeCallback));
        closeCallback.add(this::closeCallback);

        refreshCallback = new WindowRefreshCallback();
        refreshCallback.add(GLFW.glfwSetWindowRefreshCallback(openglID, refreshCallback));
        refreshCallback.add(this::refreshCallback);

        focusCallback = new WindowFocusCallback();
        focusCallback.add(GLFW.glfwSetWindowFocusCallback(openglID, focusCallback));
        focusCallback.add(this::focusCallback);

        iconifyCallback = new WindowIconifyCallback();
        iconifyCallback.add(GLFW.glfwSetWindowIconifyCallback(openglID, iconifyCallback));
        iconifyCallback.add(this::iconifyCallback);

        maximizedCallback = new WindowMaximizedCallback();
        maximizedCallback.add(GLFW.glfwSetWindowMaximizeCallback(openglID, maximizedCallback));
        maximizedCallback.add(this::maximizedCallback);

        frameBufferSizeCallback = new FrameBufferSizeCallback();
        frameBufferSizeCallback.add(GLFW.glfwSetFramebufferSizeCallback(openglID, frameBufferSizeCallback));
        frameBufferSizeCallback.add(this::frameBufferSizeCallback);

        charCallback = new CharCallback();
        charCallback.add(GLFW.glfwSetCharCallback(openglID, charCallback));
        charCallback.add(this::charCallback);

        charModsCallback = new CharModsCallback();
        charModsCallback.add(GLFW.glfwSetCharModsCallback(openglID, charModsCallback));
        charModsCallback.add(this::charModsCallback);

        keyCallback = new KeyCallback();
        keyCallback.add(GLFW.glfwSetKeyCallback(openglID, keyCallback));
        keyCallback.add(this::keyCallback);
        keyCallback.add(Keyboard::callback);

        mousePositionCallback = new MousePositionCallback();
        mousePositionCallback.add(GLFW.glfwSetCursorPosCallback(openglID, mousePositionCallback));
        mousePositionCallback.add(this::mousePositionCallback);
        mousePositionCallback.add(Mouse::positionCallback);

        mouseButtonCallback = new MouseButtonCallback();
        mouseButtonCallback.add(GLFW.glfwSetMouseButtonCallback(openglID, mouseButtonCallback));
        mouseButtonCallback.add(this::mouseButtonCallback);
        mouseButtonCallback.add(Mouse::callback);

        mouseEnteredCallback = new MouseEnteredCallback();
        mouseEnteredCallback.add(GLFW.glfwSetCursorEnterCallback(openglID, mouseEnteredCallback));
        mouseEnteredCallback.add(this::mouseEnteredCallback);
        mouseEnteredCallback.add(Mouse::enteredCallback);

        scrollCallback = new ScrollCallback();
        scrollCallback.add(GLFW.glfwSetScrollCallback(openglID, scrollCallback));
        scrollCallback.add(this::scrollCallback);
        scrollCallback.add(Mouse::scrollCallback);
    }

    /**
     * @since 1.0.0
     * Record for store important {@link GLFW} stuff like ID or NanoVG context.
     * @see NanoVG
     * @see GLFW
     * @see GL
     * @author Daniel Ramke
     * @param openglID the generated window as long address.
     * @param nvgID the context of {@link NanoVG}.
     * @param capabilities the current or created {@link GL#createCapabilities()}.
     */
    record Context(long openglID, long nvgID, GLCapabilities capabilities) { }

    /**
     * Private function to generate a UID for the window.
     * @param serialID wish ID can be replaced by null for {@link Class#getSimpleName()}.
     * @return String - the free ID for this window.
     */
    private String generateSerialID(String serialID) {
        if(serialID == null) {
            serialID = getClass().getSimpleName();
        }
        if(UIFactory.containsWindow(serialID)) {
            logger.warn("Window serialID=" + serialID + ", already exist... generating new one...");
            int index = 1;
            for(String IDs : UIFactory.getWindows().keySet()) {
                if(IDs.startsWith(serialID) || IDs.startsWith(serialID + "-")) {
                    index++;
                }
            }
            return generateSerialID(serialID + "-" + index);
        }

        return serialID;
    }

    /**
     * Private function to print all the needed graphics information we need.
     * Note at debug log it will print a big list.
     */
    private void printGraphicCardInformation() {
        List<String> info = new ArrayList<>();
        info.add("OpenGL Version: " + Objects.requireNonNull(GL11.glGetString(GL11.GL_VERSION)).substring(0, 3));
        info.add("Graphic Card: " + GL11.glGetString(GL11.GL_RENDERER));
        info.add("Graphic Provider: " + GL11.glGetString(GL11.GL_VENDOR));
        logger.list(info, "Graphics", ConsoleColor.GREEN, LogLevel.INFO);

        String[] extensions = Objects.requireNonNull(GL11.glGetString(GL11.GL_EXTENSIONS)).split(" ");
        List<String> extendedInfo = new ArrayList<>(Arrays.asList(extensions));
        logger.empty("");
        logger.list(extendedInfo, "Graphics Extensions", ConsoleColor.CYAN, LogLevel.DEBUG);
    }

    /**
     * Private function to let wait function for {@link #build()} state.
     * Needed for function which change {@link GLFW} thinks.
     */
    private void _wait() {
        try {
            latch.await();
        } catch (InterruptedException ignored) {}
    }

}
