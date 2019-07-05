package com.oneone.framework.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ToolbarResource {

    int title() default 0;

    boolean titleAlignCenter() default true;

    int subtitle() default 0;

    int layout() default 0;

    int navigationIcon() default 0;

    int background() default 0;

    boolean immersive() default false;


}
