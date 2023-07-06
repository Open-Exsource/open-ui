package net.exsource.openui.logic;

import net.exsource.openlogger.Logger;
import net.exsource.openui.ui.UIWindow;

/**
 * This interface is used to create new renderers for the ui system.
 * If you implemented this interface you will get all needed render functions.
 * This can be used for {@link UIWindow}'s to create games or ui components.
 * Note that if you wish to create your own render system you need to write all the stuff
 * to render our components or other thinks. This make no sense if you use our library because why you use this
 * lib then?
 * @since 1.0.0
 * @see RenderPriority
 * @see UIWindow
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public interface Renderer {

    /**
     * This method returned the name of the renderer.
     * This name need be final and can't duplicate!
     * @return String - the final name as identifier.
     */
    String getName();

    /**
     * Function is used for converting a simple {@link Renderer} object to his correct class.
     * This means if you look for the correct renderer class, and you have known the class then you can
     * fill it to the type parameter. If it was a successfully convertion then the function will return
     * the right object, if it was failed the return value is {@link Renderer}.
     * @param type the class type parameter.
     * @return T - the founded object as his correct type.
     * @param <T> - the key word will be replaced by class object.
     * @exception Exception thrown if the class can't be found or casting.
     */
    default <T extends Renderer> T cast(Class<T> type) {
        T value;
        Logger logger = Logger.getLogger();
        if(type != null) {
            try {
                value = (T) Class.forName(type.getName()).getDeclaredConstructor().newInstance();
            } catch (Exception exception) {
                logger.error(exception);
                value = (T) this;
            }
        } else {
            value = (T) this;
        }
        return value;
    }

    /**
     * This method returned the render priority of the renderer.
     * This can be change all time.
     * @return RenderPriority - the current render priority.
     */
    default RenderPriority getPriority() {
        return RenderPriority.MODERATE;
    }

    /**
     * This method change the current render priority.
     * @param priority the new render priority.
     */
    void setPriority(RenderPriority priority);

    /**
     * This boolean will be return true if it was priority changes detected.
     * @return boolean - true by change detect.
     */
    boolean neededPatch();

    /**
     * This method is used by the render() method in the window class.
     * Please don't change thinks here!
     * @param need - needed an update or not (Priority update!).
     */
    void setNeededPatch(boolean need);

    /**
     * This method returned true if the renderer was successfully initialized.
     * @return the state of initialization.
     */
    boolean isInitialized();

    /**
     * This method is called at first if the renderer is starting.
     * Include here methods to called one time by starting the renderer.
     * Don't loop thinks in this method!
     */
    void initialize();

    /**
     * This method is called every tick of system.
     * This is needed to update the render all time and render graphics.
     * @param window the window witch need to be rendered.
     */
    void render(UIWindow window);

    /**
     * This method clear and destroyed the current renderer.
     * After calling this method, the renderer is not enabled more and can't be drawing thinks.
     */
    void dispose();

}
