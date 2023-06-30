package net.exsource.openui.ui;

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
    public void destroy() { /* Not in use! */ }

    @Override
    public boolean isParent() {
        return false;
    }

    public void setParent(ParentWindow parent) {
        this.parent = parent;
    }

    public ParentWindow getParent() {
        return parent;
    }
}
