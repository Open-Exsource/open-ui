package net.exsource.open.enums.geometry;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public enum Horizontal {

    /**
     * Represents nothing.
     */
    NOTHING(-1),

    /**
     * Indicates left horizontal position.
     */
    LEFT(0),

    /**
     * Indicates centered horizontal position.
     */
    CENTER(1),

    /**
     * Indicates right horizontal position.
     */
    RIGHT(2);

    private final int index;

    Horizontal(int index) {
        this.index = index;
    }

    /**
     * @return {@link Integer} - the index number for horizontal direction.
     */
    public int getIndex() {
        return this.index;
    }
}

