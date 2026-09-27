/* Standalone Gradle build stub, see ../Plugin.java for context. */
package com.android.systemui.plugins.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProvidesInterface {
    String action();
    int version();
    String parentAction() default "";
}
