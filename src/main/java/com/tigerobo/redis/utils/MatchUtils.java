package com.tigerobo.redis.utils;

import java.util.HashSet;
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
        while (m.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            res.add(m.group());//获取当前匹配的值
        }

      return res;
    }
}
