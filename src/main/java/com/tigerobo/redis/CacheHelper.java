package com.tigerobo.redis;

import com.tigerobo.redis.proxy.MethodInvoker;
import com.tigerobo.redis.template.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;

public class CacheHelper  {



    public Object registerOperatorWithTemplate(Object template){

        if(template instanceof RedisTemplate){
            return new RedisCache(template);
        }
        return null;
    }


    public Object getValueByMethodAndTemplate(Method anntationBindMethod,Object template,Object[] args){
        MethodInvoker mi = new MethodInvoker(template,anntationBindMethod,args);
        try {
            return mi.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
