package net.exsource.open.annotation.component;

import net.exsource.open.logic.renderer.UIRenderer;
import net.exsource.open.ui.UIWindow;
import net.exsource.open.ui.component.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation is used in class {@link UIRenderer} for initialize the current drawing window
 * to the given {@link Component}. The component have the field window which is a type of
 * {@link UIWindow}. If the field is marked by this annotation it will be set the {@link UIWindow}
 * by save the component in the render que. This annotation will only work for this and
 * is completely in internal usage. Note if you wish to use this annotation you need to create
 * your own Reflection handling system. We ar not supporting experimental systems from third party program's.
 * For more information visiting our website: <a href="https://www.exsource.de">click here</a>.
 * @since 1.0.0
 * @see UIRenderer
 * @see Component
 * @see UIWindow
 * @author Daniel Ramke
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SetComponentWindow { }
