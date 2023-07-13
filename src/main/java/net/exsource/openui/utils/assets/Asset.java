package net.exsource.openui.utils.assets;

import java.util.LinkedList;
import java.util.List;

public class Asset<T extends IAsset> {

    private final List<T> intern = new LinkedList<>();

    public void add(T asset) {
        if(!contains(asset))
            intern.add(asset);
    }

    public void remove(T asset) {
        if(contains(asset))
            intern.remove(asset);
    }

    public T find(String name) {
        T found = null;
        for(T entries : intern) {
            if(entries.getName().equals(name)) {
                found = entries;
                break;
            }
        }
        return found;
    }

    public boolean contains(T asset) {
        if(asset == null) return false;
        return find(asset.getName()) != null;
    }

}
