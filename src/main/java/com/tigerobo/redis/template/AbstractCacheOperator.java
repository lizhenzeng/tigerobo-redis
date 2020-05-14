package com.tigerobo.redis.template;

import com.tigerobo.redis.CacheHelper;
import com.tigerobo.redis.annotation.WrapAnnotation;
import com.tigerobo.redis.bind.AnnotationBindClassRegister;
import com.tigerobo.redis.operator.WrapClass;
import javassist.NotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

abstract class AbstractCacheOperator {
    public static AnnotationBindClassRegister abcr = new AnnotationBindClassRegister();

    public static CacheHelper cacheHelper = new CacheHelper();

    public Object getValueByAnnotation( Annotation ce, Object template, String operatorMethodName, Map<String,Object> otherArgs) throws NotFoundException {
        if(abcr!=null && ce!=null && template!=null && cacheHelper!=null){
            WrapAnnotation wa = new WrapAnnotation.Builder().setAnnotation(ce).setBindMethodName(operatorMethodName).build();
            mergeArgs(wa,otherArgs,false);
            WrapClass wc = new WrapClass.Builder().setClassObject(template).setBindMethodName(operatorMethodName).build();
            if(!abcr.isRegister(wa)){
                abcr.registerBind(wa,wc,wa.getMa());
            }
            Method bindMethod = abcr.getBindMethod(wa);
            mergeArgs(wa,otherArgs,true);
            return cacheHelper.getValueByMethodAndTemplate(bindMethod,template,wa.getArgs(bindMethod,wc));
        }
        return null;
    }

    public void mergeArgs(WrapAnnotation wa,Map<String,Object> otherArgs,boolean isOverWrite){
        if(otherArgs!=null && otherArgs.size()>0){
            otherArgs.entrySet().forEach(
                    val->{
                        if(wa.getMa().getObject(val.getKey())!=null){
                            if(isOverWrite){
                                wa.getMa().putMetaValue(val.getKey(),val.getValue());
                            }
                        }else{
                            wa.getMa().putMetaValue(val.getKey(),val.getValue());
                        }
                    }
            );
        }

    }
}
