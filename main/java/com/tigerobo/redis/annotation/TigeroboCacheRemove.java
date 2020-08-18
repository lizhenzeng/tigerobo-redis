package com.tigerobo.redis.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TigeroboCacheRemove {
    String key() default "";
    String[] keys() default "";
    Class template();
}
