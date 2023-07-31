package net.exsource.open.logic.renderer.util;

import net.exsource.open.ErrorHandler;
import net.exsource.open.enums.Errors;
import net.exsource.open.ui.UIWindow;
import net.exsource.openlogger.Logger;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public class NanoVGText {

    private static final Logger logger = Logger.getLogger();

    private final long ID;
    private final UIWindow window;

    private boolean error;

    public NanoVGText(@NotNull UIWindow window) {
        this.window = window;
        this.ID = window.getContext().nvgID();
        if(ID <= MemoryUtil.NULL) {
            ErrorHandler.handle(Errors.WINDOW_NOT_CONTAINS_NVG);
        }
    }
}
