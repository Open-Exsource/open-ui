package net.exsource.openui.annotations.gen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for handling UID genration. With the type parameter you can choose
 * the string argument typo. If you wish to change the complexity then use the complexity parameter.
 * Default typo: UUID_FORMAT, complexity: NORMAL.
 * @deprecated this annotation will be moved to OpenUtils.
 */
@Deprecated
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GenID {

    String type() default "UUID_FORMAT";

    String complexity() default "NORMAL";
}
