package com.tigerobo.redis.template;

import com.tigerobo.redis.CacheHelper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class CacheConfiguration {

    @AutoConfigureAfter({RedisTemplate.class})
    @Configuration
    static class RedisCacheFactory implements CacheFactory {



        @Bean
        public CacheManager createWrapCacheManager(RedisTemplate redisTemplate){
            CacheManager  cacheManager = new CacheManager();
            CacheHelper cacheHelper = new CacheHelper();
            if(cacheManager.isNeedRegisterCache(redisTemplate)){
                cacheManager.registerCache(redisTemplate,cacheHelper);
            }

            return  cacheManager;
        }

    }
}
