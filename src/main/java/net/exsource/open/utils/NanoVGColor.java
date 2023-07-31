package net.exsource.open.utils;

import net.exsource.openutils.tools.Color;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

/**
 * Class to convert normal {@link Color} objects to an {@link NVGColor} object.
 * This is needed for working with {@link NanoVG} and his dependencies.
 * @since 1.0.0
 * @see Color
 * @see NVGColor
 * @see NanoVG
 * @author Daniel Ramke
 */
public class NanoVGColor {

    /**
     * Function which converts {@link Color}'s to {@link NVGColor}'s.
     * @param color the {@link Color} object to convert.
     * @return {@link NVGColor} - converted {@link Color}.
     */
    public static NVGColor convert(@NotNull Color color) {
        NVGColor nvgColor = NVGColor.calloc();

        nvgColor.r(color.getPercentRed());
        nvgColor.g(color.getPercentGreen());
        nvgColor.b(color.getPercentBlue());
        nvgColor.a(color.getPercentAlpha());

        return nvgColor;
    }
}
