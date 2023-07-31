package net.exsource.open.ui.style.generic;

import net.exsource.open.enums.geometry.TextDirection;
import net.exsource.openutils.tools.Color;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public class FontLook {

    private double size;
    private double blur;
    private double lineHeight;
    private String face;
    private TextDirection direction;
    private Color color;

    public FontLook(double size, double blur, double lineHeight, String face, TextDirection direction, Color color) {
        this.size = size;
        this.blur = blur;
        this.lineHeight = lineHeight;
        this.face = face;
        this.direction = direction;
        this.color = color;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getSize() {
        return size;
    }

    public void setBlur(double blur) {
        this.blur = blur;
    }

    public double getBlur() {
        return blur;
    }

    public void setLineHeight(double lineHeight) {
        this.lineHeight = lineHeight;
    }

    public double getLineHeight() {
        return lineHeight;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getFace() {
        return face;
    }

    public void setDirection(TextDirection direction) {
        this.direction = direction;
    }

    public TextDirection getDirection() {
        return direction;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
