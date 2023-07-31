package net.exsource.open.annotation;

import net.exsource.openlogger.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @since 1.0.0
 * @author Daniel Ramke
 */
public final class AnnotationProcessor {

    private static final Logger logger = Logger.getLogger();

    /**
     * Function to invoke {@link Annotation}'s, note that this function only invoke {@link Annotation} which
     * declared on functions which not have parameters. This function can be used for handle self calling functions
     * in your {@link Class}es.
     * @param object the called class object which contains the {@link Annotation}'s.
     * @param annotation the {@link Annotation} which need to be invoked.
     * @apiNote This function is currently in test phase please report thinks like unknown errors or wrong usages.
     * Note that this is important for better performance or bug handling.
     * @see Annotation
     * @see Class
     */
    public static void invoke(Class<?> object, Class<? extends Annotation> annotation) {
        invoke(object, annotation, true);
    }

    /**
     * Function to invoke {@link Annotation}'s, note that this function only invoke {@link Annotation} which
     * declared on functions which not have parameters. This function can be used for handle self calling functions
     * in your {@link Class}es.
     * @param object the called class object which contains the {@link Annotation}'s.
     * @param annotation the {@link Annotation} which need to be invoked.
     * @param allowAccess grant access to the called functions.
     * @apiNote This function is currently in test phase please report thinks like unknown errors or wrong usages.
     * Note that this is important for better performance or bug handling.
     * @see Annotation
     * @see Class
     */
    public static void invoke(Class<?> object, Class<? extends Annotation> annotation, boolean allowAccess) {
        if(object == null) {
            logger.warn("Can't invoke a null object!");
            return;
        }

        final Method[] methods = object.getDeclaredMethods();

        try {
            for(Method method : methods) {
                Annotation handle = method.getAnnotation(annotation);
                if(handle == null)
                    continue;

                if(method.getParameterTypes().length != 0)
                    continue;

                if(allowAccess)
                    method.setAccessible(true);

                method.invoke(object.getDeclaredConstructor().newInstance());
                logger.debug("Called successfully " + method.getName() + "!");
            }
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

}
