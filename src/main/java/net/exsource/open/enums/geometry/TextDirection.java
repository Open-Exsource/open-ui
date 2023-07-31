package net.exsource.open.enums.geometry;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public enum TextDirection {

    HORIZONTAL(0),
    VERTICAL_TOP_DOWN(1),
    VERTICAL_DOWN_TOP(2);

    private final int direction;

    TextDirection(int direction) {
        this.direction = direction;
    }

    /**
     * @return {@link Integer} - the current direction as id.
     */
    public int getDirection() {
        return this.direction;
    }

}

