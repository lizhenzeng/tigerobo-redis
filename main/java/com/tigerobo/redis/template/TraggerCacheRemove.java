package com.tigerobo.redis.template;

import com.tigerobo.redis.annotation.TigeroboCacheRemove;
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
public class TraggerCacheRemove extends AbstractCacheOperator {

    @Autowired
    CacheManager cacheManager;


    @Pointcut("@annotation(com.tigerobo.redis.annotation.TigeroboCacheRemove)")
    public void cacheRemove(){

    }

    @Around("cacheRemove()")
    public void process(ProceedingJoinPoint pjp) {
        MethodSignature targetMethodSignature =(MethodSignature) pjp.getSignature();
        TigeroboCacheRemove ce = targetMethodSignature.getMethod().getDeclaredAnnotation(TigeroboCacheRemove.class);
        Map<String,Object>  otherArgs =  ParserKeyHelper.getKeyByOgnl(pjp,ce,new String[]{"key","keys"});
        try {
            if(ce!=null){
                Object template =  null;
                if(ce.template()!=null && template == null){
                    template = cacheManager.getTemplateInstanceByClassName(ce.template().getName());
                }
                getValueByAnnotation(ce,template,"remove",otherArgs);
                pjp.proceed();
            }
        } catch (Throwable throwable) {
            try {
                pjp.proceed();
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
            }
        }
    }

}
