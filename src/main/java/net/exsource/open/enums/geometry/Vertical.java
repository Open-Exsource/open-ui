package net.exsource.open.enums.geometry;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public enum Vertical {

    /**
     * Represents nothing.
     */
    NOTHING(-1),

    /**
     * Indicates top vertical position
     */
    TOP(0),

    /**
     * Indicates centered vertical position
     */
    CENTER(1),

    /**
     * Indicates bottom vertical position
     */
    BOTTOM(2);

    private final int index;

    Vertical(int index) {
        this.index = index;
    }

    /**
     * @return {@link Integer} - the index number for vertical direction.
     */
    public int getIndex() {
        return this.index;
    }


}
