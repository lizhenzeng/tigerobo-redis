package com.tigerobo.redis.bind;

import com.tigerobo.redis.annotation.MetaAnnotation;
import com.tigerobo.redis.utils.ReflectUtils;
import com.tigerobo.redis.utils.Validation;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class AbstractBindRegister implements BindRegister {


    public Map<String, Method> bind( MetaAnnotation ms,String targetMethodName,String sourceMethodName,Object target,Object source,String id) throws NoSuchMethodException {
        Map<String,Method> bindMap = new HashMap<>();
        if(source instanceof Annotation){
            ms.registerAnnotation((Annotation)source).parser();
            Method proxyMethod = getMethodCandiatateByArgs(ms,sourceMethodName,target);
            if(proxyMethod!=null){
                bindMap.put(sourceMethodName,proxyMethod);
            }
        }
        return bindMap;
    }

    public Method getMethodCandiatateByArgs(MetaAnnotation ms,String sourceMethodName,Object target){
        try {


            for(Method m: ReflectUtils.getClazzOfCtMethods(target.getClass())){
                if (m.getName().equalsIgnoreCase(sourceMethodName) ) {
                    if(ReflectUtils.isCanditateByParamterName(m,ms)){
                        return m;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static Method getCtMethodMappingMethod(CtMethod cm,Class clazz) throws NotFoundException, NoSuchMethodException {
        CtClass[] ccs = cm.getParameterTypes();
        List<Class> clzz = Arrays.stream(ccs).map(val-> {
            try {
                if(Validation.equalTrimIgnoreCase(val.getName(),"java.lang.String[]")){
                    return String[].class;
                }else{
                    return Class.forName(val.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).filter(val->val!=null).collect(Collectors.toList());
        return clazz.getDeclaredMethod(cm.getName(),clzz.toArray(new Class[clzz.size()]));
    }



}
