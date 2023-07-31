package net.exsource.open.ui.modals;

import net.exsource.openutils.tools.Color;

/**
 * Record is used to control color portion compare to other {@link Color}'s.
 * This record is using in {@link ColorGradient}.
 * @since 1.0.0
 * @see Color
 * @author Daniel Ramke
 * @param color the wish color.
 * @param portion the percentage portion.
 */
public record ColorStop(Color color, float portion) {

    /**
     * Loaded default parameters for {@link ColorStop}, because
     * null objects ar not stable to use!
     * @param color the wish color.
     * @param portion the percentage portion.
     */
    public ColorStop {
        if(color == null) {
            color = Color.FALLBACK_COLOR;
        }
        if(portion < 0) {
            portion = 0;
        }
    }

}
