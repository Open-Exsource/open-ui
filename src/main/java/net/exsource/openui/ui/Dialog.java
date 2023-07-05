package net.exsource.openui.ui;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.enums.StringPattern;
import net.exsource.openutils.tools.Commons;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class Dialog {

    private static final Logger logger = Logger.getLogger();
    private static final List<Dialog> currentDialogs = new ArrayList<>();

    public static final Integer DEFAULT_WIDTH = 600;
    public static final Integer DEFAULT_HEIGHT = 350;

    private final CountDownLatch wait;

    private final String identifier;
    private long openglID;
    private UIWindow holder;

    private String title;

    private int width;
    private int height;

    public Dialog(String identifier) {
        this.wait = new CountDownLatch(1);
        this.identifier = checkIdentifier(identifier);
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.title = getClass().getSimpleName();
        build();
        configureToHolder();

        currentDialogs.add(this);
    }

    protected abstract void initialize();
    protected abstract void update();
    protected abstract void clear();

    public String getIdentifier() {
        return identifier;
    }

    public long getOpenglID() {
        _wait();
        return openglID;
    }

    public void setHolder(UIWindow holder) {
        this.holder = holder;
    }

    public UIWindow getHolder() {
        return holder;
    }

    public static Dialog get(@NotNull String ID) {
        Dialog result = null;
        for(Dialog dialog : currentDialogs) {
            if(dialog.getIdentifier().equals(ID)) {
                result = dialog;
                break;
            }
        }
        return result;
    }

    public static List<Dialog> getCurrentDialogs() {
        return currentDialogs;
    }

    protected void build() {

    }

    protected void configureToHolder() {

    }

    private String checkIdentifier(String identifier) {
        if(identifier == null) {
            identifier = Commons.generateString(StringPattern.ALPHABETIC, 5);
        }

        if(get(identifier) != null) {
            int index = 0;
            for(Dialog dialog : currentDialogs) {
                if(dialog.getIdentifier().startsWith(identifier) || dialog.getIdentifier().startsWith(identifier + "-")) {
                    index++;
                }
            }
            identifier = identifier + "-" + index;
        }
        return identifier;
    }

    private void _wait() {
        try {
            wait.await();
        } catch (InterruptedException ignored) {}
    }
}
