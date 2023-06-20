package net.exsource.openui;

import net.exsource.openlogger.Logger;
import net.exsource.openlogger.util.ConsoleColor;
import net.exsource.openui.system.Property;
import net.exsource.openui.system.PropertyValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ToDo: class commit...
 *
 * @since 1.0.0
 * @see Property
 *
 * @author Daniel Ramke
 */
public final class Registry {

    private static final Logger logger = Logger.getLogger();

    private static final List<Property> properties = new ArrayList<>();

    public static void registerProperties(String[] properties) {
        if(properties == null || properties.length == 0) {
            logger.warn("Properties array is null or empty. Please only give an array with content!");
            return;
        }
        registerProperties(Arrays.asList(properties));
    }

    public static void registerProperties(@NotNull List<String> properties) {
        if(properties.isEmpty()) {
            logger.warn("Properties list is empty and can't converted!");
            return;
        }
        for(String property : properties) {
            String[] elements = property.split("[:=]");
            if(elements.length < 2) {
                logger.warn("Property: " + ConsoleColor.YELLOW + property + ConsoleColor.RESET + " is not in the correct format!");
                logger.warn("Please look at our documentation: https://exsource.de/faq/documentations/open-ui/properties");
                continue;
            }
            String key = elements[0];
            String value = elements[1];
            registerProperty(key, value);
        }
    }

    /**
     * This function is used to register properties for the project.
     * It is the way to register as example program arguments or from .properties files.
     * The key is used as identifier for the value which is behind the key.
     *
     * @param key the property key used for find it.
     * @param value the value behind the property key.
     *
     * @see Property
     * @see PropertyValue
     */
    public static void registerProperty(@NotNull String key, Object value) {
        if(key.isEmpty() || key.isBlank()) {
            logger.warn("A property key can't be blank or empty!");
            return;
        }

        Property property = getProperty(key);
        if(property != null) {
            logger.warn("Property key " + ConsoleColor.YELLOW + key + ConsoleColor.RESET + " was found and override's with value: "
                    + ConsoleColor.YELLOW + value + ConsoleColor.RESET);
            property.setValue(value);
        } else {
            property = new Property(key, value);
            properties.add(property);
        }
    }

    public static void unregisterProperty(@NotNull Property property) {
        unregisterProperty(property.getKey());
    }

    public static void unregisterProperty(@NotNull String property) {
        if (properties.isEmpty()) {
            logger.warn("No properties was found at Registry!");
            return;
        }
        Property toRemove = null;
        for(Property values : properties) {
            if(values.getKey().equals(property)) {
                toRemove = values;
            }
        }
        properties.remove(toRemove);
        logger.debug("Removed property " + ConsoleColor.CYAN + property + ConsoleColor.RESET);
    }

    public static Property getProperty(@NotNull String key) {
        Property property = null;
        if (properties.isEmpty()) {
            logger.warn("No properties was found at Registry!");
            return null;
        }
        for (Property values : properties) {
            if (values.getKey().equals(key)) {
                property = values;
                break;
            }
        }
        return property;
    }

    public static List<Property> getProperties() {
        return properties;
    }
}
