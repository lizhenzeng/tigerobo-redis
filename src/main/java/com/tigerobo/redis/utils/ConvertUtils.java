package com.tigerobo.redis.utils;

import java.util.Set;

public class ConvertUtils {
    public static <T> T convertObjectToT(Object value, Class<T> tClass) {
        if (value != null && value instanceof String) {
            Set<String> res = MatchUtils.getRegValueFromStr("(-){0,1}([0-9]+)", (String) value);
            if (res != null && !res.isEmpty()) {
                if (tClass.isAssignableFrom(Integer.class)) {
                    return (T) Integer.valueOf(res.iterator().next());
                }
            }
        } else if (value != null && value instanceof Long && tClass.isAssignableFrom(Integer.class)) {
            return (T) new Integer(((Long) value).intValue());
        }
        return null;
    }
}

