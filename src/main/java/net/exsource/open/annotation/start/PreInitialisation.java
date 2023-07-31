package net.exsource.open.annotation.start;

import net.exsource.open.annotation.AnnotationProcessor;

import java.lang.annotation.*;

/**
 * Annotation for handling your own pre initialisation code.
 * Note you can use this {@link Annotation} on your methods in the main caller class.
 * It is absolute important that the call come from the same {@link Class} you call
 * this {@link Annotation}. The {@link AnnotationProcessor} looks for the main {@link Class}
 * to know at which location you need this {@link Annotation}.
 * @since 1.0
 * @see Annotation
 * @see AnnotationProcessor
 * @author Daniel Ramke
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreInitialisation { }
