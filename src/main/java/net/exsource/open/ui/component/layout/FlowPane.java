package net.exsource.open.ui.component.layout;

import net.exsource.open.ui.component.Component;
import net.exsource.open.ui.style.Style;

public class FlowPane extends Layout {

    public FlowPane() {
        this(null);
    }

    public FlowPane(String localizedName) {
        super(localizedName);
        this.setSize(200, 200);
    }

    @Override
    protected void logic() {
        for(Component component : getChildren()) {
            Style style = component.getStyle();
            for(Component components : getChildren()) {
                if(components.equals(component))
                    continue;

                if(style.getPosition().equalsIgnoreCase("absolute")) {
                    //Todo: set logic for absolute.
                    continue;
                }


            }
        }
    }
}
