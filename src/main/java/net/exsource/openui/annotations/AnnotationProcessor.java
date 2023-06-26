package net.exsource.openui.annotations;

import net.exsource.openlogger.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationProcessor {

    private static final Logger logger = Logger.getLogger();

    public static void invokeAnnotation(Class<?> object, Class<? extends Annotation> annotation) {
        invokeAnnotation(object, annotation, true);
    }

    public static void invokeAnnotation(Class<?> object, Class<? extends Annotation> annotation, boolean makeAccessAble) {
        if(object == null)
            return;

        final Method[] methods = object.getDeclaredMethods();

        try {
            for(Method method : methods) {
                Annotation handler = method.getAnnotation(annotation);
                if(handler == null)
                    continue;

                if (method.getParameterTypes().length != 0)
                    continue;

                if(makeAccessAble) {
                    method.setAccessible(true);
                }
                method.invoke(object.getDeclaredConstructor().newInstance());
            }
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

}
