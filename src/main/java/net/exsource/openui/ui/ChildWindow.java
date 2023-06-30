package net.exsource.openui.ui;

import net.exsource.openui.events.windows.WindowDestroyEvent;
import net.exsource.openutils.event.EventManager;

public abstract class ChildWindow extends AbstractWindow {

    private ParentWindow parent;

    public ChildWindow(String serialID) {
        super(serialID);
        this.parent = null;
    }

    protected abstract void initialization();
    protected abstract void update();
    protected abstract void clear();

    @Override
    public void run() { /* Not in use! */ }

    @Override
    public void destroy() {
        EventManager.callEvent(new WindowDestroyEvent(this));
    }

    @Override
    public boolean isParent() {
        return false;
    }

    public boolean hasParent() { return parent != null; }

    public void setParent(ParentWindow parent) {
        this.parent = parent;
    }

    public ParentWindow getParent() {
        return parent;
    }
}
