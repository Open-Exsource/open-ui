package net.exsource.openui;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestManager {

    @BeforeAll
    static void testSetup() {
        OpenUI.launch(new String[]{});
    }

    @Test
    void checkWindowCreation() throws InterruptedException {

    }

}
