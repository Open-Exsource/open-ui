package net.exsource.open.ui.component.layout;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public class Pane extends Layout {

    public Pane() {
        this(null);
    }

    public Pane(String localizedName) {
        super(localizedName);
        this.setSize(200, 200);
    }

    @Override
    protected void logic() { /* has no logic */ }
}
