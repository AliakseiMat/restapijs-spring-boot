package com.restapijs.annotation;

import java.lang.annotation.*;

/**
 * Created by aliakseimatsarski on 1/28/17.
 */
@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface JsRestModelConfig {
    String name() default "";
    String fileName() default "";
    String path() default "";
}
