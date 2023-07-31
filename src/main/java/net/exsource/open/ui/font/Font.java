package net.exsource.open.ui.font;

import net.exsource.open.UIFactory;
import net.exsource.open.utils.UIUtils;
import net.exsource.openlogger.Logger;
import net.exsource.openutils.tools.Commons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling font and his {@link FontFace}'s.
 * This class provided functions to generate fonts and save them to
 * the UIRegistry.
 * @since 1.0.0
 * @see FontFace
 * @author Daniel Ramke
 */
public class Font {

    private final Logger logger = Logger.getLogger();

    private final List<FontFace> faces = new ArrayList<>();

    private String path;
    private String name;
    private String reason;

    private boolean created;
    private boolean updated;

    /**
     * Constructor to generate a font from path and name.
     * @param path the font folder path.
     * @param name the name of the font.
     */
    public Font(String path, String name) {
        this(path + "/" + name);
    }

    /**
     * Constructor for generating a new font by absolutePath.
     * This constructor is the default one which means it is recommended to use
     * this one.
     * @param absolutePath path to the font include fontname.
     */
    public Font(String absolutePath) {
        if(absolutePath == null || absolutePath.isBlank() || absolutePath.isEmpty()) {
            logger.warn("We can't create a font object from an empty or null path!");
            return;
        }
        this.name = Commons.getFileName(absolutePath);
        this.path = absolutePath.substring(0, (absolutePath.length() - name.length()));
        this.loadFaces();
        if(created) {
            logger.debug("Font " + name + ", successfully created!");
            UIFactory.registerFont(this);
            return;
        }
        logger.error("Can't create font " + name + ", reason: " + reason);
    }

    /**
     * Function to update the current font faces.
     */
    public void update() {
        updated = false;
        this.loadFaces();
        logger.debug("Font " + getName() + ", successfully updated!");
    }

    /**
     * @return {@link String} - the font name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return {@link String} - only the font path.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return {@link String} - the complete font path.
     */
    public String getAbsolutePath() {
        return getPath() + getName();
    }

    /**
     * @param name face name for searching.
     * @return {@link Boolean} - true if the face exist in the {@link List} of faces.
     */
    public boolean hasFace(String name) {
        return getFace(name) != null;
    }

    /**
     * Function to give a specified {@link FontFace} by the name of it.
     * @param name the font face name.
     * @return {@link FontFace} - the founded face can be null.
     */
    public FontFace getFace(String name) {
        if(faces.isEmpty()) return null;
        String formatted;
        if(!name.contains("-")) {
            formatted = getName() + "-" + name;
        } else {
            formatted = name;
        }
        FontFace fontFace = null;
        for(FontFace face : faces) {
            if(face.getName().equals(formatted)) {
                fontFace = face;
                break;
            }
        }
        return fontFace;
    }

    /**
     * @return {@link List} - the current {@link FontFace}'s as list.
     */
    public List<FontFace> getFaces() {
        return faces;
    }

    /**
     * @return {@link Boolean} - the state of creation.
     */
    public boolean isCreated() {
        return created;
    }

    /**
     * @return {@link Boolean} - the state of update.
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Main function for generating the {@link FontFace}'s for this {@link Font}.
     * This is called at the default constructor to generate the {@link Font} by
     * the given name and path. It will look for available {@link FontFace}'s in
     * the {@link Font} folder.
     * @see FontFace
     */
    private void loadFaces() {
        if(created && updated)
            return;

        File folder = new File(getAbsolutePath());
        if(!folder.exists()) {
            reason = "Font folder dosen't exists!";
            return;
        }

        File[] files = folder.listFiles();
        if(files == null) {
            reason = "No font files was found in folder: " + folder.getPath();
            return;
        }

        int valid = 0;
        int invalid = 0;
        int duplicated = 0;

        for(File file : files) {
            String fileName = file.getName();
            if(!(fileName.endsWith(".ttf")) || !(fileName.contains("-"))) {
                invalid++;
                continue;
            }

            String[] build = fileName.split("-");
            if(build.length > 2) {
                invalid++;
                continue;
            }
            String fontName = build[0];
            String faceName = build[1];

            if(!(getName().equals(fontName))) {
                invalid++;
                continue;
            }
            String[] array = faceName.split("\\.");
            if(array.length > 2) {
                invalid++;
                continue;
            }
            String saveName = getName() + "-" + array[0];
            if(hasFace(saveName)) {
                if(!updated)
                    continue;

                duplicated++;
                continue;
            }
            faces.add(new FontFace(getAbsolutePath(), saveName));
            valid++;
        }
        logger.info("Finished Font loading for " + getName()
                + ", with ( Valid: " + valid + " | Invalid: " + invalid
                + " | Duplicated: " + duplicated + " ), faces!");
        this.created = true;
        this.updated = true;
    }

    /**
     * Static variant of {@link Font} as Fallback.
     */
    public static final Font FALLBACK = new Font(UIUtils.internalPath("assets/font/"), "Fallback");
}
