package net.exsource.open.ui.modals;

import net.exsource.open.UIFactory;
import net.exsource.openlogger.Logger;
import net.exsource.openutils.tools.Commons;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class is for creating an image object.
 * Note that these objects are not loaded into the intended NanoVG context.
 * The creation of an OpenGL id is done in the renderer itself, since the correct context is also available there.
 * In addition, the methods getWidth() and getHeight() depend on the creation of the OpenGL id.
 * For more information visit our website: <a href="https://www.exsource.de">click here</a>.
 * @since 1.0.0
 * @author Daniel Ramke
 */
public class Image {

    private final Logger logger = Logger.getLogger();

    private final String[] allowedFormats = new String[]{"png", "jpg", "jpeg", "svg", "gif"};

    private final Map<Long, Integer> gl_func_id_list = new HashMap<>();
    private final String path;
    private final String name;
    private final String type;

    private int imageWidth;
    private int imageHeight;
    private int[] pixelData;

    private float alpha;

    private boolean initialize;

    /**
     * Constructor creates an image object which stored the needed information.
     * The constructor can't create the needed id.
     * This id is created by trying rendering the image.
     * @param path the correct image path.
     */
    public Image(@NotNull String path) {
        this.path = path;
        this.name = Commons.getOnlyFileName(path);
        this.type = Commons.getFileType(path);
        if(!checkFormat()) {
            logger.error("Doesn't support " + type + ", as valid format!");
            return;
        }
        this.setAlpha(1.0f);
        this.createInformation();
        initialize = true;
        UIFactory.registerImage(this);
    }

    /**
     * Function added a new context with id.
     * Please don't use this method by your self.
     * This method is used in internal function, which need this id.
     * If you need an identifier for the image use the getName() method instance of this.
     * @param context the nvg render context.
     * @param gl_func_id the created id from nvg.
     */
    public void addId(long context, int gl_func_id) {
        if(gl_func_id_list.containsKey(context)) {
            return;
        }
        gl_func_id_list.put(context, gl_func_id);
    }

    /**
     * Function gets the id by contextID.
     * Note that this id is only useful to opengl.
     * The return value can be -1, this means that this context have no image id!
     * @param context the nvg context which stored the correct id.
     * @return int - the opengl id from nvg renderer.
     */
    public int getGl_Func_Id(long context) {
        return gl_func_id_list.getOrDefault(context, -1);
    }

    /**
     * @return String - the current image path.
     */
    public String getPath() {
        return path;
    }

    /**
     * This returned the file name, this is useful as identifier.
     * @return String - final name of this image.
     */
    public String getName() {
        return name;
    }

    /**
     * This returned the image type, the type is mean as the file extension.
     * @return String - final type of this image.
     */
    public String getType() {
        return type;
    }

    /**
     * @return String[] - all allowed {@link Image} formats.
     */
    public String[] getAllowedFormats() {
        return allowedFormats;
    }

    /**
     * @return float - the current memory size from the file.
     */
    public float getMemSize() {
        try {
            InputStream stream = Commons.resurceToInputStream(getPath());
            float size = (float) stream.available() / (long) 1e+6;
            stream.close();
            return size;
        } catch (IOException e) {
            logger.fatal("Can't find file by path: " + getPath());
            return -1;
        }
    }

    /**
     * @return int - the image width. (final width)
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @return int - the image height. (final height)
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * Function changed the current alpha value of this image.
     * The alpha component is only supported by PNG formats.
     * This method checks the given image file for the formats.
     * The default value of alpha component is 1.0f.
     * @param alpha the opacity of the image.
     */
    public void setAlpha(float alpha) {
        if(alpha > 1.0f || alpha < 0.0f) {
            alpha = 1.0f;
        }
        if(!getFormat().equals(".png")) {
            alpha = 1.0f;
        }
        this.alpha = alpha;
    }
    /**
     * @return float - the current alpha value.
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * @return int - the current pixels in the image.
     */
    public int getPixels() {
        return imageWidth * imageHeight;
    }

    /**
     * This method returned the created pixel data array.
     * The array contains all colors for the pixels.
     * @return int[] - the complete array of colored pixels.
     */
    public int[] getPixelData() {
        return pixelData;
    }

    /**
     * @return boolean - true if the id list is not empty.
     */
    public boolean isCreated() {
        return !gl_func_id_list.isEmpty();
    }

    public boolean isInitialize() {
        return initialize;
    }

    /**
     * @return String - the file extension which defined the image format.
     */
    public String getFormat() {
        return Commons.getFileType(path);
    }

    /**
     * Function created the needed image information for us.
     * This will be absolute needed to create the correct {@link Image}.
     * The width and height will be cached or all the colors.
     * This function using {@link BufferedImage} to store and read the current
     * {@link Image} data.
     * @see BufferedImage
     */
    private void createInformation() {
        try {
            BufferedImage image = ImageIO.read(Commons.resurceToInputStream(path));
            this.imageWidth = image.getWidth();
            this.imageHeight = image.getHeight();
            this.pixelData = new int[imageWidth * imageHeight];
            int[] tmpData = new int[imageWidth * imageHeight];
            image.getRGB(0, 0, imageWidth, imageHeight, tmpData, 0, imageWidth);
            for(int i = 0; i < tmpData.length; i++) {
                pixelData[i] = (tmpData[i] >> 16) & 0xff;
                pixelData[i] = (tmpData[i] >> 8) & 0xff;
                pixelData[i] = (tmpData[i]) & 0xff;
                pixelData[i] = (tmpData[i] >> 24) & 0xff;
            }
        } catch (IOException exception) {
            logger.error(exception);
        }
    }

    /**
     * Private function to check if the current created {@link Image} a
     * valid {@link Image} or not.
     * @return boolean - validation check state, true means it is valid.
     */
    private boolean checkFormat() {
        boolean state = false;
        for(String format : allowedFormats) {
            if(format.equalsIgnoreCase(type)) {
                state = true;
                break;
            }
        }
        return state;
    }

    /**
     * Function create a new image by the given path.
     * If the path be found than it will return the existing image.
     * @param path the new image path.
     * @param alpha the image alpha value.
     * @return Image - the image witch was found or new created.
     */
    public static Image create(@NotNull String path, float alpha) {
        Image image = Image.get(Commons.getOnlyFileName(path));
        if(image == null) {
            image = new Image(path);
            image.setAlpha(alpha);
        }
        if(!image.isInitialize())
            image = FALLBACK;

        return image;
    }

    /**
     * Function create a new image by the given path.
     * If the path be found than it will return the existing image.
     * @param path the new image path.
     * @return Image - the image witch was found or new created.
     */
    public static Image create(@NotNull String path) {
        return create(path, 1.0f);
    }

    /**
     * Function get a created image by name.
     * @param name the final image name (without extension).
     * @return Image the founded image can be null!
     */
    public static Image get(@NotNull String name) {
        return UIFactory.getImage(name);
    }

    /**
     * Static variant of {@link Image} as Fallback.
     */
    public static Image FALLBACK = new Image(""); //Todo: set a image.

}
