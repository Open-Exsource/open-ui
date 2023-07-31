package net.exsource.open.ui;

import net.exsource.open.logic.Renderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.GL11;

public abstract class AbstractWindow extends UIWindow {

    private boolean allowNVG;
    private boolean renderAtIconified;

    public AbstractWindow() {
        super(null);
        allowNVG = true;
        renderAtIconified = false;
    }

    public AbstractWindow(String identifier) {
        super(identifier);
    }

    protected abstract void update(float delta);
    protected abstract void render();

    @Override
    protected void initialize() {
        build();
        defaultConfigure();
    }

    @Override
    protected void loop() {
        long lastTime = System.nanoTime();
        double fps = 60.0; //Todo: make this change able.
        double nanos = 1_000_000_000.0 / fps;
        double delta = 0;

        while (!willClose()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nanos;
            lastTime = now;

            if(delta >= 1.0) {
                update((float) (delta * fps));
                delta = 0;
            }

            renderCheck();
            if(!isIconified()) {
                renderImpl();
            } else {
                if(renderAtIconified) {
                    renderImpl();
                }
            }

            GLFW.glfwSwapBuffers(openglID);
            GLFW.glfwPollEvents();
        }
    }

    public void setRenderAtIconified(boolean renderAtIconified) {
        this.renderAtIconified = renderAtIconified;
    }

    public boolean isRenderAtIconified() {
        return renderAtIconified;
    }

    public void setAllowNVG(boolean allowNVG) {
        this.allowNVG = allowNVG;
    }

    public boolean isAllowNVG() {
        return allowNVG;
    }

    private void renderImpl() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(background.getPercentRed(), background.getPercentGreen()
                , background.getPercentBlue(), background.getPercentAlpha());
        if(allowNVG) {
            NanoVG.nvgBeginFrame(context.nvgID(), getWidth(), getHeight(), 1f);

            render();
            for(Renderer renderer : getRenderers()) {
                renderer.render(this);
            }

            NanoVG.nvgRestore(context.nvgID());
            NanoVG.nvgEndFrame(context.nvgID());
        } else {
            render();
            for(Renderer renderer : getRenderers()) {
                renderer.render(this);
            }
        }
    }

    private void renderCheck() {
        for(Renderer renderer : getRenderers()) {
            if(!renderer.isInitialized()) {
                renderer.initialize();
            }
        }
    }
}
