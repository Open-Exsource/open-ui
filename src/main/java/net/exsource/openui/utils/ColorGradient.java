package net.exsource.openui.utils;

import net.exsource.openlogger.Logger;
import net.exsource.openui.logic.renderer.util.NanoVGBackground;
import net.exsource.openutils.tools.Color;

/**
 * Class generates color gradients as css format. This is needed for better
 * background options in {@link NanoVGBackground}, or other classes which need
 * gradients. Note that this class is not perfect yet and can be change every time.
 * @since 1.0.0
 * @see ColorStop
 * @see Color
 * @author Daniel Ramke
 */
public class ColorGradient {

    private static final Logger logger = Logger.getLogger();

    private Color start = Color.FALLBACK_COLOR;
    private Color end = Color.FALLBACK_COLOR;

    private final ColorStop[] complexGradient;
    private Direction direction;
    private float angle;

    /**
     * Constructor creates a new gradient with the specified colors.
     * Please don't choose more than 2 colors!
     * @param colors - choose 2 colors for create a color gradient.
     * @see ColorStop
     * @see Color
     */
    public ColorGradient(Color... colors) {
        if(colors == null) {
            logger.warn("ColorGradients need min tow different colors!");
            this.complexGradient = null;
            return;
        }

        if(colors.length <= 1) {
            logger.warn("ColorGradients need min tow different colors!");
            this.complexGradient = null;
            return;
        }

        if(colors.length == 2) {
            start = colors[0];
            end = colors[1];
        } else {
            start = colors[0];
            end = colors[colors.length - 1];
        }

        this.direction = Direction.TOP_TO_BOTTOM;
        this.setAngle(direction.getAngle());
        this.complexGradient = new ColorStop[colors.length];
        for(int i = 0; i < colors.length; i++) {
            complexGradient[i] = new ColorStop(colors[i], (float) i / (float) (colors.length - 1));
        }
    }

    /**
     * @return Color - the current selected start color.
     */
    public Color getStart() {
        return start;
    }

    /**
     * @return Color - the current selected end color.
     */
    public Color getEnd() {
        return end;
    }

    /**
     * @return Color[] - all chosen colors.
     */
    public ColorStop[] getColors() {
        return complexGradient;
    }

    /**
     * @return Direction - the current gradient direction.
     * Is NONE if the angle not in a correct direction.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Function set the current angle.
     * You can choose the direction you wish from 0 to 360.
     * But you can switch the sum by 1 to 1 instanceof the setAngle(Direction) method.
     * @param angle - the angle from 0 to 360.
     */
    public void setAngle(float angle) {
        if(angle < 0 || angle > 360) {
            angle = Direction.TOP_TO_BOTTOM.getAngle();
        }
        this.angle = angle;
        this.direction = Direction.get(angle);
    }

    /**
     * Function set the current angle.
     * You can choose the direction you wish from 0 to 360.
     * @param direction - the wish direction.
     */
    public void setAngle(Direction direction) {
        this.setAngle(direction.getAngle());
    }

    /**
     * @return float - the current angle.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * This enum constance stored the known css gradient values.
     * With these values you can change the displayed direction of your gradient.
     * Note that this enum is only used for the gradient class and will not work if you try it in other classes.
     */
    public enum Direction {
        TOP_LEFT_TO_BOTTOM_RIGHT("to bottom right", 45),
        TOP_TO_BOTTOM("to bottom", 90),
        TOP_RIGHT_TO_BOTTOM_LEFT("to bottom left", 135),
        CENTER_RIGHT_TO_CENTER_LEFT("to left", 180),
        BOTTOM_RIGHT_TO_TOP_LEFT("to top left", 225),
        BOTTOM_TO_TOP("to top", 270),
        BOTTOM_LEFT_TO_TOP_RIGHT("to top right", 315),
        CENTER_LEFT_TO_CENTER_RIGHT("to right", 360),
        NONE("none", 0);

        private final String cssName;
        private final float angle;

        /**
         * Internal constructor for initialization of enum constance's.
         * @param cssName the readable name of css values.
         * @param angle the angle which is stored in enum constance.
         */
        Direction(String cssName, float angle) {
            this.cssName = cssName;
            this.angle = angle;
        }

        /**
         * @return String - the identifier css value.
         */
        public String getCssName() {
            return cssName;
        }

        /**
         * @return float - the angle radius.
         */
        public float getAngle() {
            return angle;
        }

        /**
         * Function returned the founded enum object by css code.
         * @param css - the linear-gradient(to right) code.
         * @return Direction - the founded direction.
         */
        public static Direction get(String css) {
            Direction direction = NONE;
            for(Direction directions : values()) {
                if(directions.getCssName().equals(css)) {
                    direction = directions;
                    break;
                }
            }
            return direction;
        }

        /**
         * Function returned the founded enum object by angle.
         * @param angle - the angle direction.
         * @return Direction - the founded direction.
         */
        public static Direction get(float angle) {
            Direction direction = NONE;
            for(Direction directions : values()) {
                if(directions.getAngle() == angle) {
                    direction = directions;
                    break;
                }
            }
            return direction;
        }
    }
}
