package net.exsource.openui.utils;

import net.exsource.openutils.tools.Color;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.nanovg.NVGColor;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public class NanoVGColor {

    public static NVGColor convert(@NotNull Color color) {
        NVGColor nvgColor = NVGColor.calloc();

        nvgColor.r(color.getPercentRed());
        nvgColor.g(color.getPercentGreen());
        nvgColor.b(color.getPercentBlue());
        nvgColor.a(color.getPercentAlpha());

        return nvgColor;
    }
}
