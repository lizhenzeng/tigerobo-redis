package com.tigerobo.redis.template;

import com.tigerobo.redis.CacheHelper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfiguration {

    @AutoConfigureAfter({RedisTemplate.class})
    @Configuration
    static class RedisCacheFactory implements CacheFactory {


        @Bean
        public CacheManager createWrapCacheManager(RedisConnectionFactory redisConnectionFactory, RedisTemplate redisTemplate) {
            redisTemplate.setConnectionFactory(redisConnectionFactory);
//        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
            GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
            // 设置值（value）的序列化采用FastJsonRedisSerializer。
            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
            // 设置键（key）的序列化采用StringRedisSerializer。
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.afterPropertiesSet();
            CacheManager cacheManager = new CacheManager();
            CacheHelper cacheHelper = new CacheHelper();
            if (cacheManager.isNeedRegisterCache(redisTemplate)) {
                cacheManager.registerCache(redisTemplate, cacheHelper);
            }

            return cacheManager;
        }

    }
}
