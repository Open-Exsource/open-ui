package net.exsource.open.ui.font;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.tools.Commons;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Class for store font faces which can be used by a font.
 * The class generates a {@link ByteBuffer} from the given resource.
 * This resource is protected for garbage collecting.
 * @since 1.0.0
 * @see ByteBuffer
 * @author Daniel Ramke
 */
public class FontFace {

    private final Logger logger = Logger.getLogger();

    private final String path;
    private final String name;
    private ByteBuffer buffer;

    /**
     * Constructor to generate a new {@link FontFace} for the font class.
     * Note that this constructor needs 2 parameters and not empty or blank.
     * @param path the correct path to the face.
     * @param name the name of the font face without extension like .ttf
     */
    public FontFace(@NotNull String path, @NotNull String name) {
        this.path = path;
        this.name = name;
        this.generateBuffer();
    }

    /**
     * @return {@link String} - the correct file path to the font.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return {@link String} - the correct face name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return {@link ByteBuffer} - the generated {@link ByteBuffer}.
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * Private function for generate {@link ByteBuffer} from a resource.
     * Note that the converted we use ar {@link Commons#resourceToByteBuffer(String)}.
     * This generated the resource as {@link ByteBuffer} with the auto located size.
     * @see ByteBuffer
     * @see Commons
     */
    private void generateBuffer() {
        if(path.isEmpty() || path.isBlank() || name.isEmpty() || name.isBlank()) {
            logger.warn("Wrong face detected we need an path and a name of font face!");
            return;
        }

        String resource = path + "/" + name + ".ttf";
        buffer = Commons.resourceToByteBuffer(resource);
        if(buffer == null)
            logger.error("The given resource can't be generated as by buffer!");
    }
}
