package net.exsource.openui.style.generic;

import net.exsource.openui.utils.ColorGradient;
import net.exsource.openui.utils.Image;
import net.exsource.openutils.enums.Colors;
import net.exsource.openutils.math.Radius;
import net.exsource.openutils.tools.Color;

/**
 * Class creates a background object for us.
 * Depending on the type, a background object can have new properties.
 * The default type is COLOR.
 * With this type we are allowed to use a single color as background.
 * With the type LINEAR_GRADIENT you can use a gradient.
 * The last supported type is IMAGE. As the name suggests, the image type allows you to use an image as a background.
 * Note that all other methods that do not belong to the area of application do not transmit correct values.
 * For more information visit our website: <a href="https://www.exsource.de">click here</a>.
 * @since 1.0.0
 * @see Color
 * @see Image
 * @see ColorGradient
 * @author Daniel Ramke
 */
public class Background {

    private Type type;

    private Color color;
    private Image image;
    private ColorGradient gradient;
    private Radius radius;

    /**
     * Constructor creates an empty background instance.
     * The instance is created with color GRAY, image NULL and gradient NULL.
     * The radius of the default background is 0.
     * @param type the using background type.
     */
    public Background(Type type) {
        this.type = type;
        this.setRadius(new Radius(0));
        this.setColor(Color.FALLBACK_COLOR);
        this.setImage(null);
        this.setGradient(ColorGradient.get(Color.named(Colors.LIGHTGRAY), Color.named(Colors.GRAY)));
    }
    /**
     * Constructor creates a new background object with a specified color.
     * The constructor called the default constructor too.
     * @param color the specified color.
     */
    public Background(Color color) {
        this(Type.COLOR);
        this.setColor(color);
    }

    /**
     * Constructor creates a new background object with a specified image.
     * The constructor called the default constructor too.
     * @param image the specified image.
     */
    public Background(Image image) {
        this(Type.IMAGE);
        this.setImage(image);
    }

    /**
     * Constructor creates a new background object with a specified color gradient.
     * The constructor called the default constructor too.
     * @param gradient the specified color gradient.
     */
    public Background(ColorGradient gradient) {
        this(Type.LINEAR_GRADIENT);
        this.setGradient(gradient);
    }


    /**
     * Function change the type of this background.
     * @param type the new used type.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * @return Type - the current used background type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Function change the background color.
     * @param color tne new color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Function returned the current color.
     * Note if the type not COLOR the return is by default GRAY.
     * @return Color - the current loaded color.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Function change the background image.
     * @param image tne new image.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Function returned the current image.
     * Note if the type not IMAGE the return is by default NULL.
     * @return Image - the current loaded image.
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Function change the background gradient.
     * @param gradient tne new gradient.
     */
    public void setGradient(ColorGradient gradient) {
        this.gradient = gradient;
    }

    /**
     * Function returned the current gradient.
     * Note if the type not LINEAR_GRADIENT the return is by default NULL.
     * @return ColorGradient - the current loaded gradient.
     */
    public ColorGradient getGradient() {
        return this.gradient;
    }

    /**
     * @param radius change the current background radius.
     */
    public void setRadius(Radius radius) {
        this.radius = radius;
    }

    /**
     * @return Radius - the current used background radius.
     */
    public Radius getRadius() {
        return this.radius;
    }

    /**
     * Function creates a new background color type.
     * @param color the color for the background object.
     * @return Background - created background object.
     */
    public static Background get(Color color) {
        return new Background(color);
    }

    /**
     * Function creates a new background image type.
     * @param image the image for the background object.
     * @return Background - created background object.
     */
    public static Background get(Image image) {
        return new Background(image);
    }

    /**
     * Function creates a new background color gradient type.
     * @param gradient the color gradient for the background object.
     * @return Background - created background object.
     */
    public static Background get(ColorGradient gradient) {
        return new Background(gradient);
    }

    /**
     * Enum provides the existing background types.
     * The types ar needed for the renderer it will call the correct methods.
     * In the type enum, you can get the attribute names from css for help.
     * If you use css instanceof the default builder system note that this is not all attribute names which exist!
     * For more information visit our website <a href="https://www.exsource.de">click here</a>.
     */
    public enum Type {

        IMAGE("image", new String[]{"url"}),
        COLOR("color", new String[]{"rgb", "rgba", "#"}),
        LINEAR_GRADIENT("color", new String[]{"linear-gradient"});

        private final String tag;
        private final String[] css_func_aliases;

        Type(String tag, String[] css_func_aliases) {
            this.tag = tag;
            this.css_func_aliases = css_func_aliases;
        }

        /**
         * @return String - the named end tag from css variables.
         */
        public String getTag() {
            return tag;
        }

        /**
         * @return String[] - the aliases which contains in properties of css attributes.
         */
        public String[] getCss_func_aliases() {
            return css_func_aliases;
        }
    }

}
