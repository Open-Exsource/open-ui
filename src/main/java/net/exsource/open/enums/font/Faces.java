package net.exsource.open.enums.font;

import net.exsource.open.ui.font.FontFace;

/**
 * Enum which contains default font faces.
 * The enum contains names and id of the font faces.
 * @since 1.0.0
 * @see FontFace
 * @author Daniel Ramke
 */
public enum Faces {

    REGULAR("Regular", 0),
    BOLD("Bold", 1),
    BOLD_ITALIC("BoldItalic", 2),
    ITALIC("Italic", 3),
    LIGHT("Light", 4),
    LIGHT_ITALIC("LightItalic", 5),
    THIN("Thin", 6),
    THIN_ITALIC("ThinItalic", 7),
    MEDIUM("Medium", 8),
    MEDIUM_ITALIC("MediumItalic", 9),
    BLACK("Black", 10),
    BLACK_ITALIC("BlackItalic", 11);

    private final String name;
    private final int index;

    /**
     * Constructor for default enum generation.
     * @param name the face name.
     * @param index the index number for sorting.
     */
    Faces(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * @return {@link String} - the face name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return {@link Integer} - the face sort index number.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param name face name as search criteria.
     * @return {@link Faces} - the enum object which was found by the given name.
     */
    public static Faces get(String name) {
        Faces faces = REGULAR;
        for(Faces face : values()) {
            if(face.getName().equals(name)) {
                faces = face;
                break;
            }
        }
        return faces;
    }

    /**
     * @param index face index number for search criteria.
     * @return {@link Faces} - the enum object which was found by the given index.
     */
    public static Faces get(int index) {
        Faces faces = REGULAR;
        for(Faces face : values()) {
            if(face.getIndex() == index) {
                faces = face;
                break;
            }
        }
        return faces;
    }

    /**
     * @return {@link String} - the face name as lower case, this is not the correct name of a face!
     * for the correct name use the method getName();
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
