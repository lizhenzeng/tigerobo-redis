package com.tool.example.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 *
 * @author zcc ON 2018/3/19
 **/
@Configuration
public class RedisConfig {

//    @Bean
//    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
////        CacheManager cacheManager = new RedisCacheManager(redisTemplate);
////        return cacheManager;
//        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
//        // 多个缓存的名称,目前只定义了一个
//        rcm.setCacheNames(Arrays.asList("default"));
//        //设置缓存默认过期时间(秒)
//        rcm.setDefaultExpiration(600);
//        return rcm;
//    }

    // 以下两种redisTemplate自由根据场景选择
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule()); // new module, NOT JSR310Module;
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        mapper.activateDefaultTyping(BasicPolymorphicTypeValidator.builder().build(), ObjectMapper.DefaultTyping.NON_FINAL);

        serializer.setObjectMapper(mapper);
        redisTemplate.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
//        StringRedisSerializer stringRedisSerializer =new StringRedisSerializer();
//        redisTemplate.setValueSerializer(stringRedisSerializer);
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashValueSerializer(stringRedisSerializer);
//        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }
}
