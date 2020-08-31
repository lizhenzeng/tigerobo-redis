package com.tigerobo.redis.utils;

import java.util.Set;

public class ConvertUtils {
    public static Integer convertObjectToInteger(Object value){
        if(value!=null && value instanceof String){
            Set<String> res = MatchUtils.getRegValueFromStr("(-){0,1}([0-9]+)", (String) value);
            if(res!=null && !res.isEmpty()){
                return Integer.valueOf(res.iterator().next());
            }
        }
        return 0;
    }
}

