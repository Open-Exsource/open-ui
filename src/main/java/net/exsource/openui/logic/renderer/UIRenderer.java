package net.exsource.openui.logic.renderer;

import net.exsource.openlogger.Logger;
import net.exsource.openui.annotations.AnnotationProcessor;
import net.exsource.openui.annotations.component.SetComponentWindow;
import net.exsource.openui.logic.AbstractRenderer;
import net.exsource.openui.ui.UIWindow;
import net.exsource.openui.ui.component.Component;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
            Class<?> object = component.getClass().getSuperclass();
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
}
