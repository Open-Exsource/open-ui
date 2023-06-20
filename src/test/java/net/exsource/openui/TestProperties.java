package net.exsource.openui;

import net.exsource.openui.system.Property;
import net.exsource.openui.system.PropertyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestProperties {

    private static final String[] program_arguments = new String[]{"ui-style:windows-xp", "ui-window-logic=single", "ui-wrong-type"};

    @Test
    void checkRegisterProperty() {
        Registry.registerProperties(program_arguments);
        Property key = Registry.getProperty("ui-style");
        Assertions.assertNotNull(key);

        PropertyValue value = key.getValue();
        Assertions.assertEquals("windows-xp", value.asString());

        Property needNulL = Registry.getProperty("ui-wrong-type");
        Assertions.assertNull(needNulL);
    }

    @Test
    void checkUnRegisterProperty() {
        Registry.registerProperties(program_arguments);

        Property key = Registry.getProperty("ui-style");
        Assertions.assertNotNull(key);

        PropertyValue value = key.getValue();
        Assertions.assertEquals("windows-xp", value.asString());

        Registry.unregisterProperty(key);
        Property needNulL = Registry.getProperty("ui-style");
        Assertions.assertNull(needNulL);
    }

}
