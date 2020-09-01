package com.tigerobo.redis.template;

import com.tigerobo.redis.annotation.TigeroboCacheIncrOrDecr;
import com.tigerobo.redis.parser.ParserKeyHelper;
import com.tigerobo.redis.utils.ConvertUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class TraggerCacheIncr extends AbstractCacheOperator {

    @Autowired
    CacheManager cacheManager;


    @Pointcut("@annotation(com.tigerobo.redis.annotation.TigeroboCacheIncrOrDecr)")
    public void cachePut(){

    }

    @Around("cachePut()")
    public Object process(ProceedingJoinPoint pjp) {
        MethodSignature targetMethodSignature =(MethodSignature) pjp.getSignature();
        TigeroboCacheIncrOrDecr ce = targetMethodSignature.getMethod().getDeclaredAnnotation(TigeroboCacheIncrOrDecr.class);
        Map<String,Object>  otherArgs =  ParserKeyHelper.getKeyByOgnl(pjp,ce,new String[]{"key","keys","expireTime","stride","start"});
        Object value = null;
        try {
            if(ce!=null){
                Object template =  null;
                if(ce.template()!=null && template == null){
                    template = cacheManager.getTemplateInstanceByClassName(ce.template().getName());
                }
                value = pjp.proceed();
                if(value == null){
                    value = ConvertUtils.convertObjectToT(getValueByAnnotation(ce,template,"increment",otherArgs)
                            ,targetMethodSignature.getMethod().getReturnType());
                    return value;
                }
            }
        } catch (Throwable throwable) {
            try {
                pjp.proceed();
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
            }
        }finally {
            return value;
        }

    }

}
