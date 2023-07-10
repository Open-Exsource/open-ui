package net.exsource.openui.logic.renderer.util;

import net.exsource.openlogger.Logger;
import net.exsource.openui.OpenUI;
import net.exsource.openui.enums.Errors;
import net.exsource.openui.style.generic.Background;
import net.exsource.openui.ui.UIWindow;
import net.exsource.openui.utils.ColorGradient;
import net.exsource.openui.utils.Image;
import net.exsource.openui.utils.NanoVGColor;
import net.exsource.openutils.math.Radius;
import net.exsource.openutils.tools.Color;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryUtil;

public class NanoVGBackground {

    private static final Logger logger = Logger.getLogger();

    private final long ID;
    private final UIWindow window;

    private boolean error;

    public NanoVGBackground(@NotNull UIWindow window) {
        this.window = window;
        this.ID = window.getContext().nvgID();
        if(ID <= MemoryUtil.NULL) {
            OpenUI.exit(Errors.WINDOW_NOT_CONTAINS_NVG);
        }
    }

    public void draw(int x, int y, int width, int height, Background background) {
        if(background == null) {
            drawColor(x, y, width, height, Color.FALLBACK_COLOR, Radius.FALLBACK_RADIUS);
            if(!error) {
                logger.error("Background object needed for render an background!");
            }
            error = true;
            return;
        }

        error = false;
        Radius radius = background.getRadius();
        switch (background.getType()) {
            case COLOR -> drawColor(x, y, width, height, background.getColor(), radius);
            case IMAGE -> drawImage(x, y, width, height, background.getImage(), radius);
            case LINEAR_GRADIENT -> drawColorGradient(x, y, width, height, background.getGradient(), radius);
        }
    }

    public void drawColor(int x, int y, int width, int height, Color color, Radius radius) {
        if(color == null)
            color = Color.FALLBACK_COLOR;

        if(radius == null)
            radius = Radius.FALLBACK_RADIUS;

        NanoVG.nvgBeginPath(ID);
        NanoVG.nvgPathWinding(ID, NanoVG.NVG_SOLID);
        createRect(x, y, width, height, radius);
        NanoVG.nvgFillColor(ID, NanoVGColor.convert(color));
        NanoVG.nvgFill(ID);
        NanoVG.nvgClosePath(ID);
    }

    public void drawImage(int x, int y, int width, int height, Image image, Radius radius) {
    }

    public void drawColorGradient(int x, int y, int width, int height, ColorGradient gradient, Radius radius) {
    }

    public long getID() {
        return ID;
    }

    public UIWindow getWindow() {
        return window;
    }

    private void createRect(int x, int y, int width, int height, Radius radius) {
        NanoVG.nvgRoundedRectVarying(ID, (float) x, (float) y, (float) width, (float) height
                , (float) radius.getTopLeft()
                , (float) radius.getTopRight()
                , (float) radius.getBottomRight()
                , (float) radius.getBottomLeft());
    }
}
