package net.exsource.openui;

import net.exsource.openlogger.Logger;
import net.exsource.openui.utils.Timer;

import java.util.concurrent.TimeUnit;

public final class UIEngine {

    private static final Logger logger = Logger.getLogger();

    public static void sync(int fps, Timer timer) {
        double lastLoopTime = timer.getLastLoopTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException exception) {
                logger.error(exception);
            }

            now = timer.getTime();
        }
    }

}
