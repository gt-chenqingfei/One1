package com.oneone.restful.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represent the parameter in the matrix URI
 *
 * @author qingfei.chen
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MatrixParameter {

    /**
     * The parameter name
     */
    String value();

}
