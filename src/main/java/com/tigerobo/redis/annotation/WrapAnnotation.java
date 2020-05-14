package com.tigerobo.redis.annotation;

import com.tigerobo.redis.operator.WrapClass;
import com.tigerobo.redis.utils.ReflectUtils;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class WrapAnnotation  {
    public Annotation annotation;
    public String bindMethodName;
    public MetaAnnotation ma = new MetaAnnotation();


    WrapAnnotation(Annotation an,String bindMethodName){
        ma.registerAnnotation(an).parser();
        this.annotation = an;
        this.bindMethodName = bindMethodName;
    }

    public String getId(){
        MetaAnnotation ma = new MetaAnnotation();
        ma.registerAnnotation(annotation);
        return ma.getId();
    }

    public void putAllArgs(Map<String,Object> map){
        map.entrySet().forEach(val->putArgs(val.getKey(),val.getValue()));
    }


    public void putArgs(String key,Object value){
        ma.putMetaValue(key,value);
    }

    public Object[] getArgs(Method method, WrapClass wc) throws NotFoundException {
        CtClass mainCtClass =  ReflectUtils.convertClass2CtClass(wc.getAnnotation().getClass());
        List<CtClass> paramterClazz = Arrays.stream(method.getGenericParameterTypes()).map(val-> {
            try {
                return ReflectUtils.convertClass2CtClass((Class) val);
            } catch (NotFoundException e) {}
            return null;
        }).collect(Collectors.toList());
        CtMethod ctMethod = mainCtClass.getDeclaredMethod(method.getName(), paramterClazz.toArray(new CtClass[paramterClazz.size()]));
        return getMethodArgsByParameterNames(ctMethod);
    }

    public Object[] getMethodArgsByParameterNames(CtMethod ctMethod ) throws NotFoundException {
        List<Object> args = new ArrayList();
        for(String p:ReflectUtils.methodParamterOriginalName(ctMethod)){
            Object arg = ma.getObject(p);
            if(arg != null){
                args.add(arg);
            }
        }

        return args.toArray(new Object[args.size()]);
    }

    public static class Builder{
        public Annotation annotation;
        public String bindMethodName;

        public Builder setAnnotation(Annotation an){
            this.annotation = an;
            return this;
        }
        public Builder setBindMethodName(String bindMethodName){
            this.bindMethodName = bindMethodName;
            return this;
        }
        public WrapAnnotation build(){
            return  new WrapAnnotation(annotation,bindMethodName);
        }
    }
}
