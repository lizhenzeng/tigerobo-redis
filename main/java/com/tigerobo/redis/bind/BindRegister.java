package com.tigerobo.redis.bind;

import com.tigerobo.redis.annotation.MetaAnnotation;

import java.lang.reflect.Method;

public interface BindRegister {

    boolean isRegister(Object source);

    void registerBind(Object source, Object target, MetaAnnotation ma );

    Method getBindMethod(Object source);
}
