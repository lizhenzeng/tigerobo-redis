package com.tigerobo.redis.annotation;

import com.tigerobo.redis.template.*;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({CacheConfiguration.class, TraggerCacheIncr.class,TraggerCacheEvict.class, TraggerCachePut.class, TraggerCacheRemove.class})
public @interface TigeroboEnableCache {
}
