package com.tigerobo.redis.parser;

import com.tigerobo.redis.annotation.MetaAnnotation;
import com.tigerobo.redis.annotation.Param;
import com.tigerobo.redis.exception.OgnlCountException;
import com.tigerobo.redis.exception.OgnlException;
import com.tigerobo.redis.utils.MatchUtils;
import com.tigerobo.redis.utils.Validation;
import ognl.Ognl;
import ognl.OgnlContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class ParserKeyHelper {

    public static Collection<Character> tokenSymbol = Collections.unmodifiableList(Arrays.asList('{','}'));



    private static final String  paramterReg ="(#\\w*\\.?\\w+)";

    public static Map<String,Object> getKeyByOgnl(ProceedingJoinPoint pjp,Annotation an,String[] methodNames){
        Map<String,Object> res = new HashMap<>();
        for(String m:methodNames){
            Object value = getKeyByArgs(pjp,an,m);
            if(value!=null){
                res.put(m,value);
            }
        }
        return res;
    }


    public static Object getKeyByArgs(ProceedingJoinPoint pjp, Annotation an, String ognalMethodName)  {
        return getKeyByArgs(pjp.getArgs(),(MethodSignature) pjp.getSignature(),an,ognalMethodName);
    }

    public static Object getKeyByArgs(Object[] args, MethodSignature processMethod, Annotation an, String ognalMethodName)  {

        Map<String,Object> argsMap = new HashMap<>();
        String[] ps  = processMethod.getParameterNames();
        Parameter[] paramters = processMethod.getMethod().getParameters();
        if(paramters!=null && paramters.length>0){
            try {
                for(int i = 0;i<paramters.length;i++){
                    Annotation parameterAnnotation = paramters[i].getAnnotation(Param.class);
                    if(parameterAnnotation instanceof Param){
                        argsMap.put(ps[i],args[i]);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if(ps.length != args.length){
            throw new OgnlCountException(String.format("process method args length not equal method paramters !"));
        }
        for(int i = 0;i<ps.length;i++){
            argsMap.put(ps[i],args[i]);
        }

        try {
            OgnlContext context = new OgnlContext(argsMap);
            Object expressStr = new MetaAnnotation().registerAnnotation(an).parser().getObject(ognalMethodName);
            if(expressStr!=null){
                if(expressStr.getClass().isAssignableFrom(String.class)){
                    return getToken((String) expressStr,context,ps);
                }
                if(expressStr.getClass().isAssignableFrom(String[].class)){
                    List<String> keys = Arrays.stream(((String[]) expressStr)).map(val->getToken(val,context,ps)).filter(val->Validation.notEmptyAndBlankStr(val)).collect(Collectors.toList());
                    if(keys!=null && !keys.isEmpty()){
                        return keys.toArray(new String[keys.size()]);
                    }else{
                        return null;
                    }
                }
            }
            return expressStr;
        }catch (Exception e){
            throw new OgnlException(String.format("parser ognl str is error stack is %s",e.getMessage()));
        }
    }

    public static String getToken(String expressStr,OgnlContext context ,String[] ps ){
        if((Validation.notEmptyAndBlankStr(expressStr) && isNeedOgnl(expressStr,tokenSymbol) )|| isContainArgsName(expressStr,ps)){
            return getTokenList(expressStr,context);
        }else{
            return expressStr;
        }
    }

    public static boolean isNeedOgnl(String value,Collection<Character> symbol){
        for(Character s:symbol){
            if(value.contains(String.valueOf(s))){
                return true;
            }
        }
        return false;
    }

    public static boolean isContainArgsName(String express,String[] args){
        if(Validation.notEmptyAndBlankStr(express)){
            for(String arg:args){
                if(express.contains(arg)){
                    return true;
                }
            }
        }
        return false;
    }


    public static String getTokenList(String value, OgnlContext context )  {
        if(Validation.notEmptyAndBlankStr(value)){
            Set<String> res = new HashSet<>();
            res = MatchUtils.getRegValueFromStr(paramterReg,value);
            Map<String,String> replaceToken = new HashMap<>();
            res.forEach(val->{
                try {
                    Object express = Ognl.parseExpression(val);
                    String keyStr = String.valueOf(Ognl.getValue(express,context,context.getRoot()));
                    replaceToken.put(val,keyStr);
                }catch (Exception e){}
            });

            return  getKeyByTokenValues(replaceToken,value);
        }
        return value;
    }

    public static void getSplitTokenList(String prefix,String suffix,String value,Set<String> res,int begin,int end){
       int startIndex = value.indexOf(prefix,begin);
       int endIndex = value.indexOf(suffix,begin);
       if(startIndex != -1 && endIndex != -1){
           if(startIndex + 1 < endIndex)
           res.add(value.substring(startIndex + 1,endIndex));
           begin = endIndex + 1;
           if(begin < end){
               getSplitTokenList(prefix,suffix,value,res,begin,end);
           }
       }
    }


    public static String getKeyByTokenValues(Map<String,String> map , String value){
        for(Map.Entry<String,String> entry:map.entrySet()){
            value = value.replaceAll(entry.getKey(),entry.getValue());
        }
        return value;
    }

}
