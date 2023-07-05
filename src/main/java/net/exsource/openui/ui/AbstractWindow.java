package net.exsource.openui.ui;

import net.exsource.openui.UIEngine;
import net.exsource.openui.utils.Timer;

//Todo: better system for fps loop.
public abstract class AbstractWindow extends UIWindow {

    private FPS fpsCap;
    private final Timer timer;

    public AbstractWindow(String identifier) {
        super(identifier);
        this.setFpsCap(FPS.F_60);
        this.timer = new Timer();
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
        float delta;
        float accumulator = 0f;
        int in_CAP = fpsCap.equals(FPS.F_UNLIMITED) ? 500 : fpsCap.getTimesAsInt();
        float interval = 1f / in_CAP;

        while (!willClose()) {
            delta = timer.getDelta();
            accumulator += delta;

            input();

            while (accumulator >= interval) {
                update();
                timer.updateUPS();
                accumulator -= interval;
            }

            timer.updateFPS();

            timer.update();

            UIEngine.sync(in_CAP, timer);
        }
    }

    public void setFpsCap(FPS fpsCap) {
        if(fpsCap == null) {
            fpsCap = FPS.F_UNLIMITED;
        }
        this.fpsCap = fpsCap;
    }

    public FPS getFpsCap() {
        return fpsCap;
    }

    public int getFPS() {
        return timer.getFPS();
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
        F_UNLIMITED(-1.0D);

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
}
