package net.exsource.openui;

import net.exsource.openlogger.Logger;
import net.exsource.openui.utils.Image;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class AssetFactory {

    private static final Logger logger = Logger.getLogger();

    private static final List<Image> images;

    static {
        images = new ArrayList<>();
    }

    public static void registerImage(@NotNull Image image) {
        if(existImage(image)) {
            logger.warn("Image " + image.getName() + " already created! Use the Image.get() function!");
            return;
        }

        logger.debug("Added new image " + image.getName());
        images.add(image);
    }

    public static void clearImages() {
        images.clear();
    }

    public static boolean existImage(@NotNull Image image) {
        return existImage(image.getName());
    }

    public static boolean existImage(@NotNull String name) {
        return getImage(name) != null;
    }

    public static Image getImage(@NotNull Image image) {
        return getImage(image.getName());
    }

    public static Image getImage(@NotNull String name) {
        Image image = null;
        for(Image images : getImages()) {
            if(images.getName().equals(name)) {
                image = images;
                break;
            }
        }
        return image;
    }

    public static List<Image> getImages() {
        return images;
    }
}
