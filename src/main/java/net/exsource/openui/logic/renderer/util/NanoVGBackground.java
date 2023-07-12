package net.exsource.openui.logic.renderer.util;

import net.exsource.openlogger.Logger;
import net.exsource.openui.OpenUI;
import net.exsource.openui.enums.Errors;
import net.exsource.openui.logic.renderer.UIRenderer;
import net.exsource.openui.style.generic.Background;
import net.exsource.openui.ui.UIWindow;
import net.exsource.openui.utils.ColorGradient;
import net.exsource.openui.utils.ColorStop;
import net.exsource.openui.utils.Image;
import net.exsource.openui.utils.NanoVGColor;
import net.exsource.openutils.math.Radius;
import net.exsource.openutils.tools.Color;
import net.exsource.openutils.tools.Commons;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryUtil;

/**
 * Class is used for allow users to render {@link Background} objects.
 * This class can use css properties as well and render Colors, Images and
 * Linear Gradients. note that {@link Image} supports .gif files but it is
 * not working currently.
 * @since 1.0.0
 * @see UIRenderer
 * @see NanoVG
 * @see UIWindow
 * @see Background
 * @see Radius
 * @author Daniel Ramke
 */
public class NanoVGBackground {

    private static final Logger logger = Logger.getLogger();

    private final long ID;
    private final UIWindow window;

    /**
     * One use variable in {@link #draw(int, int, int, int, Background)} will set
     * to true if there was no {@link Background} object was found!
     */
    private boolean error;

    /**
     * Constructor for internal use.
     * This will be called at the {@link UIRenderer#load(UIWindow)} function.
     * This is absolutely needed to use the class functions without errors.
     * Note if {@link UIWindow#getContext()} null or doesn't contain a valid {@link NanoVG} id,
     * the class will not work correctly and throw an error.
     * @param window need background functions.
     * @see UIWindow
     * @see NanoVG
     * @see UIRenderer
     */
    public NanoVGBackground(@NotNull UIWindow window) {
        this.window = window;
        this.ID = window.getContext().nvgID();
        if(ID <= MemoryUtil.NULL) {
            OpenUI.exit(Errors.WINDOW_NOT_CONTAINS_NVG);
        }
    }

    /**
     * Function draws the background by an {@link Background} object which define the
     * type of the background. Available types ar listening in {@link Background#getType()}.
     * This ar the types: COLOR, IMAGE and LINEAR_GRADIENT.
     * @param x the x position of the created object.
     * @param y the y position of the created object.
     * @param width the width of the created object.
     * @param height the height of the created object.
     * @param background {@link Background} object.
     * @apiNote This function is recommended to use. It will always use in our
     * render classes which controls backgrounds.
     * @see Background
     */
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

    /**
     * Function to draw the background in a single {@link Color}.
     * Note that if the color is null then it will replace with {@link Color#FALLBACK_COLOR}.
     * @param x the x position of the created object.
     * @param y the y position of the created object.
     * @param width the width of the created object.
     * @param height the height of the created object.
     * @param color the defined color for the object.
     * @param radius the radius for the created object.
     * @apiNote Currently is it not ready to use {@link java.awt.Color}, because our Color class can't
     * convert that.
     * @see Color
     * @see Radius
     */
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

    /**
     * Function to draw the background in a single {@link Image}.
     * The image can in format (.png, .jpeg, .jpg, .gif and .svg).
     * @param x the x position of the created object.
     * @param y the y position of the created object.
     * @param width the width of the created object.
     * @param height the height of the created object.
     * @param image the image to render.
     * @param radius the radius for the created object.
     * @apiNote {@link Image} is currently not using the function {@link Image#get(String)}. We wait for
     * the new System for handling assets.
     * @see Image
     * @see Color
     * @see Radius
     */
    public void drawImage(int x, int y, int width, int height, Image image, Radius radius) {
        if(image == null) {
            drawColor(x, y, width, height, Color.FALLBACK_COLOR, radius);
            return;
        }

        if(radius == null)
            radius = Radius.FALLBACK_RADIUS;

        NanoVG.nvgBeginPath(ID);
        try (NVGPaint paint = NVGPaint.calloc()) {
            int imageID = createImage(image);
            if(!image.isCreated()) {
                return;
            }

            NanoVG.nvgImagePattern(ID, x, y, width, height, 0, imageID, image.getAlpha(), paint);
            createRect(x, y, width, height, radius);
            NanoVG.nvgFillPaint(ID, paint);
            NanoVG.nvgFill(ID);
            NanoVG.nvgClosePath(ID);
        }
    }

    /**
     * Function to draw the background in a multiply {@link Color}'s.
     * This function will use {@link ColorGradient} to generate a gradient of colors.
     * @param x the x position of the created object.
     * @param y the y position of the created object.
     * @param width the width of the created object.
     * @param height the height of the created object.
     * @param gradient the color gradient which will be used.
     * @param radius the radius for the created object.
     * @apiNote Currently is it not ready to use {@link java.awt.Color}, because our Color class can't
     * convert that.
     * @see ColorGradient
     * @see Color
     * @see ColorStop
     * @see Radius
     */
    public void drawColorGradient(int x, int y, int width, int height, ColorGradient gradient, Radius radius) {
        if(gradient == null) {
            drawColor(x, y, width, height, Color.FALLBACK_COLOR, radius);
            return;
        }

        if(radius == null)
            radius = Radius.FALLBACK_RADIUS;

        ColorStop[] colors = gradient.getColors();
        for(int i = 0; i < colors.length - 1; i++) {
            try (NVGPaint paint = NVGPaint.calloc()) {
                float angle = gradient.getAngle();

                float centerX = (float)x + (float)width*0.5f;
                float centerY = (float)y + (float)height*0.5f;
                float xx = centerX - (float)((Math.cos(Math.toRadians(angle)) * width) * 0.5 + 0.5);
                float yy = centerY - (float)((Math.sin(Math.toRadians(angle)) * height) * 0.5 + 0.5);
                float dirX = (float) Math.cos(Math.toRadians(angle));
                float dirY = (float) Math.sin(Math.toRadians(angle));
                float step = colors[i].portion();
                float nextstep = colors[i+1].portion();

                float startX = xx + (dirX * step * (float)width);
                float startY = yy + (dirY * step * (float)height);
                float endX = xx + (dirX * nextstep * (float)width);
                float endY = yy + (dirY * nextstep * (float)height);

                Color start = colors[i].color();
                if(i > 0) {
                    start = Color.transparent;
                }

                Color end = colors[i + 1].color();

                NanoVG.nvgLinearGradient(ID, startX, startY, endX, endY
                        , NanoVGColor.convert(start), NanoVGColor.convert(end), paint);

                NanoVG.nvgBeginPath(ID);
                createRect(x, y, width, height, radius);
                NanoVG.nvgFillPaint(ID, paint);
                NanoVG.nvgFill(ID);
                NanoVG.nvgClosePath(ID);
            }
        }
    }

    /**
     * @return long - the current {@link NanoVG} context id.
     */
    public long getID() {
        return ID;
    }

    /**
     * @return {@link UIWindow} - current used window which is drawing {@link Background}'s.
     */
    public UIWindow getWindow() {
        return window;
    }

    /**
     * Private Function to generate rectangles for better {@link Background} usage.
     * This is absolute needed for render backgrounds like Images and Colors.
     * @param x the x position for the rectangle.
     * @param y the y position for the rectangle.
     * @param width the width of the rectangle.
     * @param height the height of the rectangle.
     * @param radius corner {@link Radius} of the rectangle.
     * @see Radius
     */
    private void createRect(int x, int y, int width, int height, Radius radius) {
        NanoVG.nvgRoundedRectVarying(ID, (float) x, (float) y, (float) width, (float) height
                , (float) radius.getTopLeft()
                , (float) radius.getTopRight()
                , (float) radius.getBottomRight()
                , (float) radius.getBottomLeft());
    }

    private int createImage(Image image) {
        int referenceID = image.getGl_Func_Id(ID);
        if(referenceID <= -1) {
            referenceID = NanoVG.nvgCreateImageMem(ID, 0, Commons.resourceToByteBuffer(image.getPath()));
            if(referenceID > -1) {
                image.addId(ID, referenceID);
            }
        }
        return referenceID;
    }
}
