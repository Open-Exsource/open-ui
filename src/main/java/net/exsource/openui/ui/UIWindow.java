package net.exsource.openui.ui;

import net.exsource.openlogger.Logger;
import net.exsource.openlogger.level.LogLevel;
import net.exsource.openlogger.util.ConsoleColor;
import net.exsource.openui.OpenUI;
import net.exsource.openui.UIFactory;
import net.exsource.openui.enums.Errors;
import net.exsource.openui.exception.windows.WindowCantBuildException;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class UIWindow {

    private static final Logger logger = Logger.getLogger();

    public static final Integer DEFAULT_WIDTH = 800;
    public static final Integer DEFAULT_HEIGHT = 600;

    private final CountDownLatch latch;
    private final Thread thread;

    private final String identifier;
    private long openglID;

    private final List<Dialog> dialogs = new ArrayList<>();

    protected Context context;

    private String title;

    private int width;
    private int height;

    private boolean created;

    /* ########################################################################
     *
     *                             Constructors
     *
     * ######################################################################## */

    public UIWindow() {
        this(null);
    }

    public UIWindow(String identifier) {
        this.latch = new CountDownLatch(1);
        this.identifier = generateSerialID(identifier);
        this.title = getClass().getSimpleName();
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.thread = UIFactory.generateThread(this::run, this);
        thread.start();
    }

    /* ########################################################################
     *
     *                       Implementation / abstraction
     *
     * ######################################################################## */

    protected void run() {
        initialize();
        loop();
        destroy();
    }

    protected abstract void initialize();
    protected abstract void loop();

    public abstract void destroy();

    /* ########################################################################
     *
     *                           General Getter/Setter
     *
     * ######################################################################## */

    public String getIdentifier() {
        return identifier;
    }

    public long getOpenglID() {
        _wait();
        return openglID;
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
        GLFW.glfwSetWindowTitle(getOpenglID(), title);
    }

    public String getTitle() {
        return title;
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

    /* ########################################################################
     *
     *                               Dialogs
     *
     * ######################################################################## */

    public void addDialog(@NotNull Dialog dialog) {

    }

    public void removeDialog(@NotNull Dialog dialog) {

    }

    public void removeDialog(@NotNull String ID) {

    }

    public boolean hasDialog(@NotNull Dialog dialog) {
        return hasDialog(dialog.getIdentifier());
    }

    public boolean hasDialog(@NotNull String ID) {
        return getDialog(ID) != null;
    }

    public Dialog getDialog(@NotNull Dialog dialog) {
        return getDialog(dialog.getIdentifier());
    }

    public Dialog getDialog(@NotNull String ID) {
        Dialog dialog = null;
        for(Dialog entry : dialogs) {
            if(entry.getIdentifier().equals(ID)) {
                dialog = entry;
                break;
            }
        }
        return dialog;
    }

    public void clearDialogs() {
        dialogs.clear();
    }

    public List<Dialog> getDialogs() {
        return dialogs;
    }

    /* ########################################################################
     *
     *                           Default Functions
     *
     * ######################################################################## */

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

        //Todo: register callbacks
        //Todo: register renderers (default)

        GLFW.glfwMakeContextCurrent(openglID);
        GLFW.glfwSwapInterval(0);
        logger.info("Window HID=" + openglID + ", named=" + getIdentifier());
        this.created = true;
        latch.countDown();
    }

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

    protected void calculateViewPort() {
        GL11.glViewport(0, 0, getWidth(), (int) getHeight());
    }

    /* ########################################################################
     *
     *                          GLFW Getter / Setter
     *
     * ######################################################################## */

    public void restore() {
        GLFW.glfwRestoreWindow(getOpenglID());
    }

    public void close() {
        GLFW.glfwSetWindowShouldClose(getOpenglID(), true);
    }

    public boolean willClose() {
        return GLFW.glfwWindowShouldClose(getOpenglID());
    }

    public void show() {
        if(!isVisible()) {
            GLFW.glfwShowWindow(getOpenglID());
        }
    }

    public void hide() {
        if(isVisible()) {
            GLFW.glfwHideWindow(getOpenglID());
        }
    }

    public boolean isVisible() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE;
    }

    public void setMaximized(boolean maximized) {
        if(maximized) {
            GLFW.glfwMaximizeWindow(getOpenglID());
            return;
        }
        this.restore();
    }

    public boolean isMaximized() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE;
    }

    public void setIconified(boolean iconified) {
        if(iconified) {
            GLFW.glfwIconifyWindow(getOpenglID());
            return;
        }
        this.restore();
    }

    public boolean isIconified() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE;
    }

    public void setFocused(boolean focused) {
        if(focused) {
            GLFW.glfwFocusWindow(getOpenglID());
            return;
        }
        this.restore();
    }

    public boolean isFocused() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        GLFW.glfwSetWindowAttrib(getOpenglID(), GLFW.GLFW_FLOATING, alwaysOnTop ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public boolean isAlwaysOnTop() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_FLOATING) == GLFW.GLFW_TRUE;
    }

    public void setResizeable(boolean resizeable) {
        GLFW.glfwSetWindowAttrib(getOpenglID(), GLFW.GLFW_RESIZABLE, resizeable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public boolean isResizeable() {
        return GLFW.glfwGetWindowAttrib(getOpenglID(), GLFW.GLFW_RESIZABLE) == GLFW.GLFW_TRUE;
    }

    /* ########################################################################
     *
     *                             Private / Misc
     *
     * ######################################################################## */

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

    record Context(long openglID, long nvgID, GLCapabilities capabilities) { }

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

    private void _wait() {
        try {
            latch.await();
        } catch (InterruptedException ignored) {}
    }

}
