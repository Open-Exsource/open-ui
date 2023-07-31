package net.exsource.open.enums.geometry;

import static net.exsource.open.enums.geometry.Horizontal.LEFT;
import static net.exsource.open.enums.geometry.Horizontal.RIGHT;
import static net.exsource.open.enums.geometry.Vertical.BOTTOM;
import static net.exsource.open.enums.geometry.Vertical.TOP;

/**
 * This enum controls the vertical and horizontal alignment.
 * If a component in another parent component with the tag Position.TOP_LEFT, the component will orient this pos.
 * <h3>Available Position / Alignment</h3>
 * <ul>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">TOP_LEFT</span> alignment the component to the left top conor.</li>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">TOP_CENTER</span> alignment the component to the center top border.</li>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">TOP_RIGHT</span> alignment the component to the right top conor.</li>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">CENTER_LEFT</span> alignment the component to the center left border.</li>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">CENTER</span> alignment the component to the center of the parent.</li>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">CENTER_RIGHT</span> alignment the component to the center right border.</li>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">BOTTOM_LEFT</span> alignment the component to the left bottom conor.</li>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">BOTTOM_CENTER</span> alignment the component to the center bottom border.</li>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">BOTTOM_RIGHT</span> alignment the component to the right bottom conor.</li>
 *     <li style="padding:7px 0px 7px;"><span style="color:#ababab;font-weight:bold;">NOTHING</span> reset the current alignment,.</li>
 * </ul>
 * @since 1.0.0
 * @see Vertical
 * @see Horizontal
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public enum Alignment {

    /**
     * Represents nothing.
     */
    NOTHING(Vertical.NOTHING, Horizontal.NOTHING),

    /**
     * Represents positioning on the top vertically and in the left horizontally.
     */
    TOP_LEFT(TOP, LEFT),

    /**
     * Represents positioning on the top vertically and in the center horizontally.
     */
    TOP_CENTER(TOP, Horizontal.CENTER),

    /**
     * Represents positioning on the top vertically and in the right horizontally.
     */
    TOP_RIGHT(TOP, RIGHT),

    /**
     * Represents positioning in the center vertically and in the left horizontally.
     */
    CENTER_LEFT(Vertical.CENTER, LEFT),

    /**
     * Represents positioning in the center both vertically and horizontally.
     */
    CENTER(Vertical.CENTER, Horizontal.CENTER),

    /**
     * Represents positioning in the center vertically and in the right horizontally.
     */
    CENTER_RIGHT(Vertical.CENTER, RIGHT),

    /**
     * Represents positioning in the bottom vertically and in the left horizontally.
     */
    BOTTOM_LEFT(BOTTOM, LEFT),

    /**
     * Represents positioning in the bottom vertically and in the center horizontally.
     */
    BOTTOM_CENTER(BOTTOM, Horizontal.CENTER),

    /**
     * Represents positioning in the bottom vertically and in the right horizontally.
     */
    BOTTOM_RIGHT(BOTTOM, RIGHT);

    private final Vertical vertical;
    private final Horizontal horizontal;

    /**
     * The enum constructor to create the positions.
     *
     * @param vertical   the vertical position.
     * @param horizontal the horizontal position.
     */
    Alignment(Vertical vertical, Horizontal horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
    }

    /**
     * @return {@link Vertical} - the vertical positioning / alignment.
     */
    public Vertical getVertical() {
        return vertical;
    }

    /**
     * @return {@link Horizontal} - the horizontal positioning / alignment.
     */
    public Horizontal getHorizontal() {
        return horizontal;
    }

}

