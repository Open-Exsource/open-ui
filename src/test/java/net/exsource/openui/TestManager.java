package net.exsource.openui;

import net.exsource.openui.ui.windows.Window;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TestManager {

    @BeforeAll
    static void testSetup() {
        OpenUI.launch(new String[]{});
    }

    @Test
    void checkWindowCreation() throws InterruptedException {
        Window testWindow = UIManager.createWindow(Window.class);
        Assertions.assertTrue(testWindow.isCreated());
        Assertions.assertEquals(Window.class.getSimpleName(), testWindow.getTitle());
        TimeUnit.SECONDS.sleep(2);
        testWindow.close();
        Assertions.assertFalse(testWindow.isUsed());
        TimeUnit.MILLISECONDS.sleep(100);
    }

}
