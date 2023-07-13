package net.exsource.openui.logic.cache;

import net.exsource.openui.annotations.gen.GenID;

public interface ICache {

    @GenID(complexity = "HARD")
    String getUID();

    String getName();

    String getPath();

    float getMemSize();

    void dispose();

}
