package com.tigerobo.redis.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtils {

    public static Set<String> getRegValueFromStr(String pattern, String line){

        Set<String> res = new HashSet<>();
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if(m.find()){
           for(int i=0;i<m.groupCount();i++){
               res.add(m.group(i));
           }
        }
      return res;
    }
}
