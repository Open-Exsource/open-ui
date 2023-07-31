package net.exsource.open.utils;

import java.net.URL;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public class UIUtils {

    /**
     * This method checked the given path for exist.
     * Note that this method called the resource url of the given class.
     * @param path - the extended path for the resource.
     * @return {@link String} - the complete resource path.
     */
    public static String internalPath(String path) {
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        URL url = UIUtils.class.getResource(path);
        return url == null ? "" : url.getPath();
    }


}
