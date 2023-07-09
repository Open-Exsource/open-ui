package net.exsource.openui.ui.component;

import net.exsource.openlogger.Logger;
import net.exsource.openui.UIFactory;
import net.exsource.openui.annotations.component.SetComponentWindow;
import net.exsource.openui.ui.UIWindow;
import net.exsource.openui.ui.component.shapes.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public abstract class Component {

    private static final Logger logger = Logger.getLogger();

    private static final List<String> idList;
    private static final List<Component> components;

    static {
        idList = new ArrayList<>();
        components = new ArrayList<>();
    }

    private final String localizedName;

    private int width;
    private int height;

    private int positionX;
    private int absoluteX;
    private int positionY;
    private int absoluteY;

    private final List<Component> children;
    private Component parent;

    @SetComponentWindow
    private UIWindow window = null;


    /* ########################################################################
     *
     *                             Constructors
     *
     * ######################################################################## */

    /**
     * Constructor for creating new {@link Component}'s. This constructor
     * is absolute needed to create a new component instance. For example,
     * you create an {@link Rectangle} an inherit {@link Component} to this object
     * to declared that this object is a component.
     * @param localizedName the wish identifier name.
     */
    public Component(String localizedName) {
        this.localizedName = id_check(localizedName);
        this.children = new ArrayList<>();
        this.parent = null;
        this.setSize(0);
        this.setPosition(0);
        components.add(this);
    }

    /* ########################################################################
     *
     *                             Important
     *
     * ######################################################################## */

    /**
     * @return String - the final name of this {@link Component}.
     */
    public String getLocalizedName() {
        return localizedName;
    }

    /**
     * @return String - components class name in lowercase, this is the type.
     */
    public String getType() {
        return getClass().getSimpleName().toLowerCase();
    }

    /**
     * Function set the window which hold this {@link Component}.
     * Note that this function is protected because it will come to
     * errors if you change this by your own. The {@link UIWindow} is set by
     * the system itself. Note that this function only exist if you need manual change.
     * The system will set the window by {@link SetComponentWindow} annotation.
     * @param window the holder window for this {@link Component}.
     * @see UIWindow
     */
    protected void setWindow(UIWindow window) {
        this.window = window;
    }

    /**
     * @return UIWindow - the window which hold this component.
     */
    public UIWindow getWindow() {
        return window;
    }

    /* ########################################################################
     *
     *                           Getter / Setter
     *
     * ######################################################################## */

    /**
     * Function set the width and height of the component.
     * @param width the component width.
     * @param height the component height.
     */
    public void setSize(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * Function set the width and height of the component.
     * The size is defined width and height.
     * @param size the component width and height.
     */
    public void setSize(int size) {
        this.setSize(size, size);
    }

    /**
     * Function set the new width for the component.
     * @param width the new width.
     */
    public void setWidth(int width) {
        if(width < 0) {
            width = 0;
        }
        this.width = width;
    }

    /**
     * @return int - current component width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Function set the new height for the component.
     * @param height the new height.
     */
    public void setHeight(int height) {
        if(height < 0) {
            height = 0;
        }
        this.height = height;
    }

    /**
     * @return int - current component height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Function to set the x and y position.
     * @param positionX the new x position.
     * @param positionY the new y position.
     */
    public void setPosition(int positionX, int positionY) {
        this.setPositionX(positionX);
        this.setPositionY(positionY);
    }

    /**
     * Function to set the x and y position.
     * @param position the new x and y position.
     */
    public void setPosition(int position) {
        this.setPosition(position, position);
    }

    /**
     * @param positionX set new x position.
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    /**
     * @return int - the current x position.
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * @param positionY set new y position.
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /**
     * @return int - the current y position.
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Function to set the x and y absolute position.
     * @param absoluteX the new x absolute position.
     * @param absoluteY the new y absolute position.
     */
    public void setAbsolute(int absoluteX, int absoluteY) {
        this.setPositionX(positionX);
        this.setPositionY(positionY);
    }

    /**
     * Function to set the x and y absolute position.
     * @param absolute the new x and y absolute position.
     */
    public void setAbsolute(int absolute) {
        this.setAbsolute(absolute, absolute);
    }

    /**
     * @param absoluteX set new x absolute position.
     */
    public void setAbsoluteX(int absoluteX) {
        this.absoluteX = absoluteX;
    }

    /**
     * @return int - the current x absolute position.
     */
    public int getAbsoluteX() {
        return absoluteX;
    }

    /**
     * @param absoluteY set new y absolute position.
     */
    public void setAbsoluteY(int absoluteY) {
        this.absoluteY = absoluteY;
    }

    /**
     * @return int - the current y absolute position.
     */
    public int getAbsoluteY() {
        return absoluteY;
    }

    /* ########################################################################
     *
     *                               Children
     *
     * ######################################################################## */

    /**
     * Function adds a new {@link Component} to this component as children.
     * If the child component currently a component of another component, then it will
     * change this. Note that you are not used the protected {@link #setParent(Component)} function,
     * because it will not correctly work.
     * @param child new children which will add to this {@link Component}.
     */
    public void addChild(@NotNull Component child) {
        if(hasChild(child)) {
            logger.warn("This component " + child.getLocalizedName() + ", already is an child of this component!");
            return;
        }

        logger.debug("Added new child " + child.getLocalizedName() + ", to " + getLocalizedName());
        child.setParent(this);
        children.add(child);
    }

    /**
     * Function removes a {@link Component} from the {@link #getChildren()} list.
     * If the component not a children of this component its will return and warn you.
     * @param child to remove {@link Component}.
     */
    public void removeChild(@NotNull Component child) {
        removeChild(child.getLocalizedName());
    }

    /**
     * Function removes a {@link Component} from the {@link #getChildren()} list.
     * If the component not a children of this component its will return and warn you.
     * @param localizedName the identifier from the to remove {@link Component}.
     */
    public void removeChild(@NotNull String localizedName) {
        if(!hasChild(localizedName)) {
            logger.warn("This component " + localizedName + ", is not a child of this component!");
            return;
        }

        logger.debug("Removed child " + localizedName + " from " + getLocalizedName());
        children.remove(getChild(localizedName));
    }

    /**
     * Function removes all existing {@link Component}'s from the {@link #getChildren()} list.
     */
    public void removeAllChildren() {
        children.clear();
    }

    /**
     * Function to check if the {@link Component} the parent of the child.
     * @param child as identifier for searching.
     * @return boolean - true if this is a child.
     */
    public boolean hasChild(@NotNull Component child) {
        return hasChild(child.getLocalizedName());
    }

    /**
     * Function to check if the {@link Component} the parent of the child.
     * @param localizedName as identifier for searching.
     * @return boolean - true if this is a child.
     */
    public boolean hasChild(@NotNull String localizedName) {
        return getChild(localizedName) != null;
    }

    /**
     * Function to set the current parent. Is protected because the parent is only be change
     * if this component changes his parent via {@link #addChild(Component)} by other {@link Component}.
     * Please don't use this function for setting up the parent.
     * @param parent the component which is used this as child.
     */
    protected void setParent(Component parent) {
        this.parent = parent;
    }

    /**
     * @return Component - get the current parent {@link Component}, can be null if parent field null.
     */
    public Component getParent() {
        return parent;
    }

    /**
     * @return boolean - true if this component have a parent.
     */
    public boolean hasParent() {
        return getParent() != null;
    }

    /**
     * @return boolean - true if the {@link #getChildren()} list not empty.
     */
    public boolean isParent() {
        return !getChildren().isEmpty();
    }

    /**
     * Function to get a specified {@link Component} form the children list.
     * The result can be null if no components was found.
     * @param localizedName the identifier which is needed for find the child component.
     * @return Component - the founded object, can be null if no element was found in {@link #getChildren()}.
     */
    public Component getChild(@NotNull String localizedName) {
        Component component = null;
        for(Component entry : children) {
            if(entry.getLocalizedName().equals(localizedName)) {
                component = entry;
                break;
            }
        }
        return component;
    }

    /**
     * @return List<Component> - list of components which ar children of this component.
     */
    public List<Component> getChildren() {
        return children;
    }

    /* ########################################################################
     *
     *                      Misc / Private Functions
     *
     * ######################################################################## */

    /**
     * Private function to check if a localizedName already in use.
     * If the given localizedName in use, this function will create an instance of
     * its name as localizedName-0, 1, 2... etc.
     * @param id the localizedName to check.
     * @return String - the generated or untouched localizedName.
     */
    private String id_check(String id) {
        if(id == null) {
            id = getClass().getSimpleName();
        }
        if(idList.contains(id)) {
            int index = 0;
            for(String entry : idList) {
                if(entry.startsWith(id) || entry.startsWith(id + "-")) {
                    index++;
                }
            }
            id = id + "-" + index;
        }
        idList.add(id);
        return id;
    }

    /**
     * Function cast the current {@link Component} to the given type.
     * The function can be used for safer cast.
     * @return T - the cast object.
     * @param <T> the component class which is now the key type.
     * @see EmptyComponent
     */
    @SuppressWarnings("unchecked")
    public <T> T cast() {
        T component;
        try {
            component = (T) Class.forName(getClass().getName());
        } catch (Exception exception) {
            logger.error(exception);
            component = (T) new EmptyComponent();
        }
        return component;
    }

    /* ########################################################################
     *
     *                           Static Functions
     *
     * ######################################################################## */

    /**
     * Function to get a component form the global list.
     * @param localizedName the component identifier.
     * @return Component - the founded component, null if wasn't found.
     */
    public static Component getComponent(@NotNull String localizedName) {
        Component component = null;
        for(Component entry : components) {
            if(entry.getLocalizedName().equals(localizedName)) {
                component = entry;
                break;
            }
        }
        return component;
    }

    /**
     * @return List<Component> - the global components list.
     */
    public static List<Component> getComponents() {
        return components;
    }
}
