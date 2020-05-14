package com.tigerobo.redis.bind;

import com.tigerobo.redis.annotation.MetaAnnotation;
import com.tigerobo.redis.annotation.WrapAnnotation;
import com.tigerobo.redis.operator.WrapClass;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationBindClassRegister extends AbstractBindRegister {

    public static Map<String,Method> ms = new HashMap<>();


    @Override
    public boolean isRegister(Object source) {
        if(source instanceof WrapAnnotation){
            return ms.containsKey(((WrapAnnotation) source).getBindMethodName());
        }
        return false;
    }

    @Override
    public void registerBind(Object wra, Object wc,MetaAnnotation ma ) {
        if(wra instanceof WrapAnnotation && wc instanceof WrapClass){
            try {

                Map<String, Method> bindMethods =  bind(
                        ma,
                        ((WrapClass) wc).getBindMethodName(),
                        ((WrapAnnotation) wra).getBindMethodName(),
                        ((WrapClass) wc).getAnnotation(),
                        ((WrapAnnotation) wra).getAnnotation(),
                        ((WrapAnnotation) wra).getId());
                ms.putAll(bindMethods);
            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
            }
        }
    }

    @Override
    public Method getBindMethod(Object source) {
        if(source instanceof WrapAnnotation){
            return ms.get(((WrapAnnotation) source).getBindMethodName());
        }
        return null;
    }
}
