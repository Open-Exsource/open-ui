package net.exsource.openui.utils;

import net.exsource.openutils.tools.Color;
import org.lwjgl.nanovg.NVGColor;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public class NanoVGColor {

    private Color color;

    public NanoVGColor(Color color) {
        this.color = color == null ? Color.FALLBACK_COLOR : color;
    }

    public Color getColor() {
        return color;
    }

    public NVGColor asNVGColor() {
        NVGColor nvgColor = NVGColor.calloc();

        nvgColor.r(color.getPercentRed());
        nvgColor.g(color.getPercentGreen());
        nvgColor.b(color.getPercentBlue());
        nvgColor.a(color.getPercentAlpha());

        return nvgColor;
    }
}
