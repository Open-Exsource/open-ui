package net.exsource.openui.ui;

import net.exsource.openlogger.Logger;
import net.exsource.openlogger.level.LogLevel;
import net.exsource.openlogger.util.ConsoleColor;
import net.exsource.openui.OpenUI;
import net.exsource.openui.UIFactory;
import net.exsource.openui.enums.Errors;
import net.exsource.openui.exception.windows.WindowCantBuildException;
import net.exsource.openutils.enums.StringPattern;
import net.exsource.openutils.tools.Commons;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class AbstractWindow {

    private static final Logger logger = Logger.getLogger();

    public static final Integer DEFAULT_WIDTH = 800;
    public static final Integer DEFAULT_HEIGHT = 600;

    private final CountDownLatch latch;
    private final Thread thread;

    private final String identifier;
    private long hardwareID;

    protected Context context;

    private String title;

    private int width;
    private int height;

    protected boolean used;
    private boolean created;

    public AbstractWindow(String identifier) {
        this.latch = new CountDownLatch(1);
        this.identifier = generateSerialID(identifier);
        this.title = getClass().getSimpleName();
        this.used = false;
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.thread = UIFactory.generateThread(this::run, this);
        this.thread.start();
    }

    protected void run() {
        build();
        defaultConfigure();

        initialize();
        while (!willClose()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            update();

            GLFW.glfwSwapBuffers(hardwareID);
            GLFW.glfwPollEvents();
        }
        destroy();
    }

    protected abstract void initialize();
    protected abstract void update();
    protected abstract void destroy();

    /* **************************************************************************
     *
     *                                  General
     *
     * **************************************************************************/

    public String getIdentifier() {
        return identifier;
    }

    public long getHardwareID() {
        _wait();
        return hardwareID;
    }

    public Context getContext() {
        return context;
    }

    public Thread getThread() {
        return thread;
    }

    public String getType() {
        return getClass().getSimpleName();
    }

    public void setTitle(String title) {
        if(title == null) {
            title = getClass().getSimpleName();
        }
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isCreated() {
        _wait();
        return created;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    protected void build() {
        logger.info("Build window " + getIdentifier() + "...");
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        this.hardwareID = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        if(hardwareID <= NULL) {
            this.created = false;
            logger.fatal(new WindowCantBuildException("Can't build window " + getIdentifier()));
            OpenUI.exit(Errors.WINDOW_BUILD.getCode());
            return;
        }

        //Todo: register callbacks
        //Todo: register renderers (default)

        GLFW.glfwMakeContextCurrent(hardwareID);
        GLFW.glfwSwapInterval(1);
        logger.info("Window HID=" + hardwareID + ", named=" + getIdentifier());
        this.created = true;
        latch.countDown();
    }

    protected void defaultConfigure() {
        GLCapabilities capabilities = GL.createCapabilities();
        long nvgID = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_STENCIL_STROKES | NanoVGGL3.NVG_ANTIALIAS);
        this.context = new Context(hardwareID, nvgID, capabilities);

        logger.debug("Crating context for " + getIdentifier());
        printGraphicCardInformation();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    protected void calculateViewPort() {
        GL11.glViewport(0, 0, getWidth(), (int) getHeight());
    }

    private String generateSerialID(String serialID) {
        if(serialID == null) {
            serialID = Commons.generateString(StringPattern.NUMBERS_AND_ALPHABETIC, 8);
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

    private void printGraphicCardInformation() {
        List<String> info = new ArrayList<>();
        info.add("OpenGL Version: " + Objects.requireNonNull(GL11.glGetString(GL11.GL_VERSION)).substring(0, 3));
        info.add("Graphic Card: " + GL11.glGetString(GL11.GL_RENDERER));
        info.add("Graphic Provider: " + GL11.glGetString(GL11.GL_VENDOR));
        logger.list(info, "Graphics", ConsoleColor.CYAN, LogLevel.INFO);

    }

    /* **************************************************************************
     *
     *                                  Extended
     *
     * **************************************************************************/

    private void _wait() {
        try {
            latch.await();
        } catch (InterruptedException ignored) {}
    }

    public void restore() {
        GLFW.glfwRestoreWindow(getHardwareID());
    }

    public void close() {
        GLFW.glfwSetWindowShouldClose(getHardwareID(), true);
        this.used = false;
    }

    public boolean willClose() {
        return GLFW.glfwWindowShouldClose(getHardwareID());
    }

    public void show() {
        if(!isVisible()) {
            GLFW.glfwShowWindow(getHardwareID());
        }
    }

    public void hide() {
        if(isVisible()) {
            GLFW.glfwHideWindow(getHardwareID());
        }
    }

    public boolean isVisible() {
        return GLFW.glfwGetWindowAttrib(getHardwareID(), GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE;
    }

    /**
     * This method maximized the current window object.
     * @param maximized this window.
     */
    public void setMaximized(boolean maximized) {
        if(maximized) {
            GLFW.glfwMaximizeWindow(getHardwareID());
            return;
        }
        this.restore();
    }

    /**
     * @return boolean - the window maximize state.
     */
    public boolean isMaximized() {
        return GLFW.glfwGetWindowAttrib(getHardwareID(), GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE;
    }

    /**
     * This method set the current window iconified.
     * @param iconified this window.
     */
    public void setIconified(boolean iconified) {
        if(iconified) {
            GLFW.glfwIconifyWindow(getHardwareID());
            return;
        }
        this.restore();
    }

    /**
     * @return boolean the window iconified state.
     */
    public boolean isIconified() {
        return GLFW.glfwGetWindowAttrib(getHardwareID(), GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE;
    }

    /**
     * This method focused the current window.
     * This means that the input targets this window.
     * @param focused this window.
     */
    public void setFocused(boolean focused) {
        if(focused) {
            GLFW.glfwFocusWindow(getHardwareID());
            return;
        }
        this.restore();
    }

    /**
     * @return boolean - the window focused state.
     */
    public boolean isFocused() {
        return GLFW.glfwGetWindowAttrib(getHardwareID(), GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
    }

    /**
     * This method set the window all time on the top layer of your desktop.
     * @param alwaysOnTop force always on top.
     */
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        GLFW.glfwSetWindowAttrib(getHardwareID(), GLFW.GLFW_FLOATING, alwaysOnTop ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    /**
     * @return boolean - the window always on top state.
     */
    public boolean isAlwaysOnTop() {
        return GLFW.glfwGetWindowAttrib(getHardwareID(), GLFW.GLFW_FLOATING) == GLFW.GLFW_TRUE;
    }

    /**
     * This method allowed the user to resize the current window.
     * @param resizeable state for this window.
     */
    public void setResizeable(boolean resizeable) {
        GLFW.glfwSetWindowAttrib(getHardwareID(), GLFW.GLFW_RESIZABLE, resizeable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    /**
     * @return boolean - the window can resize the state.
     */
    public boolean isResizeable() {
        return GLFW.glfwGetWindowAttrib(getHardwareID(), GLFW.GLFW_RESIZABLE) == GLFW.GLFW_TRUE;
    }

    @SuppressWarnings("unchecked")
    public <T> T casted() {
        T window = (T) this;
        try {
            window = (T) Class.forName(getClass().getName()).getDeclaredConstructor(String.class).newInstance(getIdentifier());
        } catch (Exception exception) {
            logger.error(exception);
        }
        return window;
    }

    /* **************************************************************************
     *
     *                                 Class/Enum
     *
     * **************************************************************************/

    private record Context(long openglID, long nvgID, GLCapabilities capabilities) { }

}
