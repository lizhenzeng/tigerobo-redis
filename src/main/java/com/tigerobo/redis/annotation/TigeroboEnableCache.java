package com.tigerobo.redis.annotation;

import com.tigerobo.redis.template.CacheConfiguration;
import com.tigerobo.redis.template.TraggerCacheEvict;
import com.tigerobo.redis.template.TraggerCachePut;
import com.tigerobo.redis.template.TraggerCacheRemove;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({CacheConfiguration.class, TraggerCacheEvict.class, TraggerCachePut.class, TraggerCacheRemove.class})
public @interface TigeroboEnableCache {
}
