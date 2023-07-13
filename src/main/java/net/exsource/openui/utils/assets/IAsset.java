package net.exsource.openui.utils.assets;

public interface IAsset {

    String getPath();

    String getName();

    float getMemSize();

    void dispose();

}
