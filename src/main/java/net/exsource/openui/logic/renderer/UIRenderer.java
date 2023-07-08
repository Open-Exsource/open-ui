package net.exsource.openui.logic.renderer;

import net.exsource.openui.logic.AbstractRenderer;
import net.exsource.openui.ui.UIWindow;
import net.exsource.openui.ui.component.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class UIRenderer extends AbstractRenderer {

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

    private void toQue(@NotNull Component component) {
        if(alreadyInQue(component.getLocalizedName())) {
            return;
        }
        loadedComponents.add(component);
        //Todo: call annotation for adding window.
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
