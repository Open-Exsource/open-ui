package net.exsource.open.ui.component.layout;

import net.exsource.open.logic.renderer.UIRenderer;
import net.exsource.open.ui.UIWindow;
import net.exsource.open.ui.component.Component;
import net.exsource.open.ui.style.Style;
import net.exsource.open.ui.style.generic.Background;
import net.exsource.openutils.enums.Colors;
import net.exsource.openutils.tools.Color;

/**
 * Class for default layout functions and indicate as {@link Layout} for the {@link UIRenderer}.
 * This class is really helpful to mark a class as {@link Layout} for the render pipeline.
 * If you need help by creating an own layout so see the docs of our official website.
 * @since 1.0.0
 * @author Daniel Ramke
 */
public abstract class Layout extends Component {

    /**
     * Constructor to initialize a new {@link Layout} for the extended class.
     * This is a super constructor for your own class to create a new {@link Layout}.
     * @param localizedName the localizedName of your {@link Layout}
     */
    public Layout(String localizedName) {
        super(localizedName);
        getStyle().setBackground(Background.get(Color.named(Colors.LIGHTGRAY)));
    }

    /**
     * Function which call your logic in a loop. Note that you not
     * initialize thinks in this function because it is calling in
     * {@link #update()} which is called at {@link UIRenderer#render(UIWindow)} all time.
     */
    protected abstract void logic();

    /**
     * Function for updating the current {@link Layout}.
     * Please don't call this method by your self. This function
     * is already called at {@link UIRenderer#render(UIWindow)} all time.
     */
    public void update() {
        holdComponentsInBound();
        logic();
    }

    private void holdComponentsInBound() {
        for(Component component : getChildren()) {
            Style style = component.getStyle();
            if(style.getPosition().equalsIgnoreCase("absolute")
                    || style.getPosition().equalsIgnoreCase("fixed"))
                continue;

            int currentX_Width = component.getPositionX() + component.getWidth();
            int currentY_Height = component.getPositionY() + component.getHeight();

            if(currentX_Width > getWidth()) {
                int correctPos = (getWidth() - component.getWidth());
                component.setPositionX(correctPos);
            }

            if(currentY_Height > getHeight()) {
                int correctPos = (getHeight() - component.getHeight());
                component.setPositionY(correctPos);
            }
        }
    }
}
