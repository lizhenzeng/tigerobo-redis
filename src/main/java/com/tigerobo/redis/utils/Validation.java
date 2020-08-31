package com.tigerobo.redis.utils;

import java.util.Collection;

public class Validation {

    public static boolean notEmptyAndBlankStr(String value) {
        return value != null && !value.trim().equalsIgnoreCase("") && !value.trim().equalsIgnoreCase("null");
    }

    public static boolean collectNotEmpty(Collection c) {
        return c != null && !c.isEmpty();
    }

    public static boolean equalTrimIgnoreCase(String value, String value1) {
        return value.trim().equalsIgnoreCase(value1.trim());
    }


    public static Boolean isTrue(Object value) {
        if (value!=null && value instanceof String) {
            if (Validation.notEmptyAndBlankStr((String) value) && ((String) value).equalsIgnoreCase("true")){
                return true;
            }
        }
        return false;
    }

    public static Boolean predictValueIgnoreCase(String value,String predict){
        return Validation.notEmptyAndBlankStr(value) && Validation.notEmptyAndBlankStr(predict) && value.equalsIgnoreCase(predict);
    }
}
