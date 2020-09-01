package com.tigerobo.redis.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TigeroboCacheIncrOrDecr {

    String key() default "";
    String[] keys() default "";
    String expireTime() default "";
    String stride() default "1";//步幅 键值对所对应键值进行累加
    String start() default "1";//初始化 键值对所对应键值进行累加
    String ifAbsentNeedIncr() default "0";//初始化 后是否需要新增 0 初始化后不加部幅 1 初始化后加部幅
    Class template();

}
