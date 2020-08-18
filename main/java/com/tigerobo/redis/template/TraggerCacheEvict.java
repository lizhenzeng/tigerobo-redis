package com.tigerobo.redis.template;

import com.tigerobo.redis.annotation.TigeroboCacheEvict;
import com.tigerobo.redis.parser.ParserKeyHelper;
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
public class TraggerCacheEvict extends AbstractCacheOperator {

    @Autowired
    CacheManager cacheManager;


    @Pointcut("@annotation(com.tigerobo.redis.annotation.TigeroboCacheEvict)")
    public void cacheEvict(){

    }

    @Around("cacheEvict()")
    public Object process(ProceedingJoinPoint pjp) {
        MethodSignature targetMethodSignature =(MethodSignature) pjp.getSignature();
        TigeroboCacheEvict ce = targetMethodSignature.getMethod().getDeclaredAnnotation(TigeroboCacheEvict.class);
        Map<String,Object>  otherArgs =  ParserKeyHelper.getKeyByOgnl(pjp,ce,new String[]{"key","keys","expireTime"});
        Object value = null;
        try {
            if(ce!=null){
                Object template =  null;
                if(ce.template()!=null && template == null){
                    template = cacheManager.getTemplateInstanceByClassName(ce.template().getName());
                }
                value = getValueByAnnotation(ce,template,"getValue",otherArgs);
                if(value == null){
                    value = pjp.proceed();
                    otherArgs.put("value",value);
                    getValueByAnnotation(ce,template,"putIfAbsent",otherArgs);
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
