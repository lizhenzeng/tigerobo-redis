package com.tigerobo.redis.template;

import com.tigerobo.redis.CacheHelper;

import java.util.HashMap;
import java.util.Map;

public class CacheManager  {


    public Map<String,Object> cacheRegister = new HashMap();


    public void registerCache(Object template, CacheHelper cacheHelper){

        cacheRegister.put(template.getClass().getName(),cacheHelper.registerOperatorWithTemplate(template));
    }

    public boolean isNeedRegisterCache(Object template){
        return !cacheRegister.containsKey(template.getClass().getName());
    }

    public Object getTemplateInstanceByClassName(String className){
        return cacheRegister.get(className);
    }
}
