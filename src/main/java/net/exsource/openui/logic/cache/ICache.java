package net.exsource.openui.logic.cache;

public interface ICache {

    String getUID();

    String getName();

    String getPath();

    float getMemSize();

    void dispose();

}
