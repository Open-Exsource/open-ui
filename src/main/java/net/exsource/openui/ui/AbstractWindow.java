package net.exsource.openui.ui;

import net.exsource.openlogger.Logger;
import net.exsource.openui.UIManager;
import net.exsource.openutils.enums.StringPattern;
import net.exsource.openutils.tools.Commons;

public abstract class AbstractWindow {

    private static final Logger logger = Logger.getLogger();

    public static final Integer DEFAULT_WIDTH = 800;
    public static final Integer DEFAULT_HEIGHT = 600;

    private final String serialID;

    private State state;
    private String title;

    private int width;
    private int height;

    protected boolean used;

    public AbstractWindow(String serialID) {
        this.serialID = generateSerialID(serialID);
        this.title = getClass().getSimpleName();
        this.used = false;
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.setState(State.CREATION);
    }

    public abstract void destroy();

    public abstract void run();

    public abstract boolean isParent();

    public String getSerialID() {
        return serialID;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public String getType() {
        return getClass().getSimpleName();
    }

    public void setTitle(String title) {
        if(title == null) {
            title = getClass().getSimpleName();
        }
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean isUsed() {
        return used;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    private String generateSerialID(String serialID) {
        if(serialID == null) {
            serialID = Commons.generateString(StringPattern.NUMBERS_AND_ALPHABETIC, 8);
        }
        if(UIManager.containsWindow(serialID)) {
            logger.warn("Window serialID=" + serialID + ", already exist... generating new one...");
            int index = 1;
            for(String IDs : UIManager.getRegisteredWindows().keySet()) {
                if(IDs.startsWith(serialID) || IDs.startsWith(serialID + "-")) {
                    index++;
                }
            }
            return generateSerialID(serialID + "-" + index);
        }

        return serialID;
    }

    public enum State {
        CREATION,
        INITIALIZATION,
        FINISHED
    }
}
