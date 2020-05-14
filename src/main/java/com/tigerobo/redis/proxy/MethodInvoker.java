package com.tigerobo.redis.proxy;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvoker  {

    public Object template;
    public Method method;
    public Object[] args;
    public MethodInvoker(Object template,Method method,Object[] args){
        this.template = template;
        this.args = args;
        this.method = method;
    }


    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(template,args);
    }

}
