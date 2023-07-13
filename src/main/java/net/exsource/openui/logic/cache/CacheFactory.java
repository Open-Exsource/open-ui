package net.exsource.openui.logic.cache;

import net.exsource.openlogger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Vector;

public final class CacheFactory {

    private static final Logger logger = Logger.getLogger();

    private static final List<ICache> cacheVector = new Vector<>();

    public static void add(ICache object) {

    }

    public static ICache get(@NotNull String uid) {
        return null;
    }

}
