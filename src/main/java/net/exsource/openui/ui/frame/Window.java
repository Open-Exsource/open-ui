package net.exsource.openui.ui.frame;

import net.exsource.openui.ui.AbstractWindow;
import org.lwjgl.glfw.GLFW;

public class Window extends AbstractWindow {

    public Window(String identifier) {
        super(identifier);
        setFpsCap(FPS.F_UNLIMITED);
    }

    @Override
    protected void input() {

    }

    @Override
    protected void update() {
        setTitle(getClass().getSimpleName() + ", FPS: " + getFPS());
        GLFW.glfwSwapBuffers(getOpenglID());
        GLFW.glfwPollEvents();
    }

    @Override
    public void destroy() {

    }

}
