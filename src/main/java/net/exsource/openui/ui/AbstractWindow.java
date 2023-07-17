package net.exsource.openui.ui;

import net.exsource.openui.logic.Renderer;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.GL11;

public abstract class AbstractWindow extends UIWindow {

    private FPS fpsCap;
    private int fps;

    private float frameTime;

    private boolean allowNvg;

    public AbstractWindow(String identifier) {
        super(identifier);
        this.setFpsCap(FPS.F_60);
        this.useNanoVG(true);
    }

    protected abstract void input();
    protected abstract void update();

    @Override
    protected void initialize() {
        build();
        defaultConfigure();
    }

    @Override
    protected void loop() {
        final long nanosecond = 1_000_000_000L;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        int frames = 0;
        long frameCounter = 0;

        while (!willClose()) {
            boolean nowRender = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) nanosecond;
            frameCounter += passedTime;

            input();

            while (unprocessedTime > frameTime) {
                nowRender = true;
                unprocessedTime -= frameTime;

                if(frameCounter >= nanosecond) {
                    fps = frames;
                    frames = 0;
                    frameCounter = 0;
                }
            }

            //FIXME: after change the allow state from nvg to false it will display old rectangle after all.
            if(nowRender) {
                renderCheck();
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                GL11.glClearColor(background.getPercentRed(), background.getPercentGreen(), background.getPercentBlue(), background.getPercentAlpha());
                GL11.glViewport(0, 0, getWidth(), getHeight());

                if(allowNvg) {
                    NanoVG.nvgBeginFrame(context.nvgID(), getWidth(), getHeight(), 1f);

                    for(Renderer renderer : getRenderers()) {
                        renderer.render(this);
                    }

                    NanoVG.nvgRestore(context.nvgID());
                    NanoVG.nvgEndFrame(context.nvgID());
                } else {
                    for(Renderer renderer : getRenderers()) {
                        renderer.render(this);
                    }
                }

                update();
                frames++;
            }
        }
    }

    public void useNanoVG(boolean allowNvg) {
        this.allowNvg = allowNvg;
    }

    public boolean isAllowNvg() {
        return allowNvg;
    }

    public void setFpsCap(FPS fpsCap) {
        if(fpsCap == null) {
            fpsCap = FPS.F_1000;
        }
        this.fpsCap = fpsCap;
        this.frameTime = 1.0f / fpsCap.getTimesAsInt();
    }

    public FPS getFpsCap() {
        return fpsCap;
    }

    public int getFps() {
        return fps;
    }

    public enum FPS {

        F_30(30.0D),
        F_50(50.0D),
        F_60(60.0D),
        F_90(90.0D),
        F_120(120.0D),
        F_144(144.0D),
        F_165(165.0D),
        F_240(240.0D),
        F_265(265.0D),
        F_500(500.0D),
        F_1000(1000.0D);

        private final double times;

        FPS(double times) {
            this.times = times;
        }

        public double getTimes() {
            return times;
        }

        public int getTimesAsInt() {
            return (int) getTimes();
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
