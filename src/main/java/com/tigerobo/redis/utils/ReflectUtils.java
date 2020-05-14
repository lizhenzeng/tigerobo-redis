package com.tigerobo.redis.utils;

import com.tigerobo.redis.annotation.MetaAnnotation;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class ReflectUtils {

    public static CtMethod[] getClazzOfCtMethods(Class clazz) throws NotFoundException {
        CtClass cc =  convertClass2CtClass(clazz);
        CtMethod[] cm = cc.getDeclaredMethods();
        return cm;
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

    public static boolean isCanditateByParamterName(CtMethod cm, MetaAnnotation ms){
        try {
            String[] ss =methodParamterOriginalName(cm);
            for(String s:ss){
                if(ms.getObject(s)==null){
                    return false;
                }
            }
            return true;
        }catch (Exception e){

        }
        return false;
    }
}
