package com.tigerobo.redis.utils;

import com.tigerobo.redis.annotation.MetaAnnotation;
import com.tigerobo.redis.annotation.Param;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectUtils {

    public static Method[] getClazzOfCtMethods(Class clazz) throws NotFoundException {
//        CtClass cc =  convertClass2CtClass(clazz);
//        CtMethod[] cm = cc.getDeclaredMethods();
        return clazz.getDeclaredMethods();
    }

    public static CtClass convertClass2CtClass(Class cLass) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(cLass.getName());
    }

    public static String[] methodParamterOriginalName(CtMethod cm) throws NotFoundException {
        // 使用javaassist的反射方法获取方法的参数名
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++)
            paramNames[i] = attr.variableName(i + pos);
        return paramNames;
    }

    public static boolean isCanditateByParamterName(Method cm, MetaAnnotation ms) {
        try {

            Parameter[] paramters = cm.getParameters();
            for (Parameter paramter : paramters) {
                Annotation parameterAnnotation = paramter.getAnnotation(Param.class);
                if (parameterAnnotation != null) {
                    String name = ((Param) parameterAnnotation).name();
                    if (name==null || ms.getObject(name) == null) {
                        return false;
                    }
                }else{
                    return false;
                }

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
