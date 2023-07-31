package net.exsource.open.logic.renderer;

import net.exsource.open.ErrorHandler;
import net.exsource.open.annotation.component.SetComponentWindow;
import net.exsource.open.enums.Errors;
import net.exsource.open.logic.AbstractRenderer;
import net.exsource.open.ui.UIWindow;
import net.exsource.open.ui.component.Component;
import net.exsource.open.ui.component.layout.Layout;
import net.exsource.openlogger.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//FIXME: ToQue can throw an CollectionModifiedException!
public abstract class UIRenderer extends AbstractRenderer {

    private static final Logger logger = Logger.getLogger();

    private final List<Component> loadedComponents;

    public UIRenderer(String name) {
        super(name);
        this.loadedComponents = new ArrayList<>();
    }

    @Override
    protected void func(UIWindow window) {
        for(Component component : window.getComponents()) {
            toQue(component);
        }

        for(Component layouts : loadedComponents) {
            if(layouts instanceof Layout layout) {
                layout.update();
            }
        }
        render(loadedComponents);
    }

    public abstract void render(@NotNull List<Component> components);

    public List<Component> getLoadedComponents() {
        return loadedComponents;
    }

    //FIXME: new components will be added after first run!
    private void toQue(@NotNull Component component) {
        if(alreadyInQue(component.getLocalizedName())) {
            return;
        }
        loadedComponents.add(component);
        try {
            Class<?> object = getParentClass(component.getClass());
            if(object == null) {
                ErrorHandler.handle(Errors.UI_NO_COMPONENT);
                return;
            }
            Field field = object.getDeclaredField("window");
            if(field.isAnnotationPresent(SetComponentWindow.class)) {
                field.setAccessible(true);
                field.set(component, getWindow());
                logger.debug("Annotated " + component.getLocalizedName() + ", successfully!");
            }
            field.setAccessible(false);
        } catch (Exception exception) {
            logger.error(exception);
        }
        if(component.isParent()) {
            for(Component components : component.getChildren()) {
                toQue(components);
            }
        }
    }

    private boolean alreadyInQue(@NotNull String localizedName) {
        boolean state = false;
        for(Component component : loadedComponents) {
            if(component.getLocalizedName().equals(localizedName)) {
                state = true;
                break;
            }
        }
        return state;
    }

    private Class<?> getParentClass(Class<?> object) {
        if(!object.getSimpleName().equals("Component")) {
            if(object.getSimpleName().equalsIgnoreCase("Object")) {
                return null;
            }
            return getParentClass(object.getSuperclass());
        }
        return object;
    }
}
