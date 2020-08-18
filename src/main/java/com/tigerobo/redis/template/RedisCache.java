package com.tigerobo.redis.template;

import com.tigerobo.redis.annotation.Param;
import com.tigerobo.redis.operator.CacheOperator;
import com.tigerobo.redis.utils.Validation;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RedisCache implements CacheOperator {

    public RedisTemplate redisTemplate;

    public RedisCache(Object redisTemplate){
        if(redisTemplate instanceof RedisTemplate){
            this.redisTemplate = (RedisTemplate) redisTemplate;
        }
    }


    @Override
    public void putIfAbsent(@Param(name = "keys") String[] keys,@Param(name = "value") Object value,@Param(name = "expireTime") String expireTime) {
        Arrays.stream(keys).forEach(val->putIfAbsent(val,value,expireTime));
    }

    @Override
    public void putIfAbsent(@Param(name = "key")String key,@Param(name = "value")  Object value,@Param(name = "expireTime")  String expireTime) {
        try {
            if(redisTemplate!=null && Validation.notEmptyAndBlankStr(expireTime)&& Validation.notEmptyAndBlankStr(key)  && value !=null){
                Long expire =  Long.valueOf(expireTime);
//                Duration duration =  Duration.ofMillis(expire);
                redisTemplate.opsForValue().setIfAbsent(key,value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void putIfAbsent(@Param(name = "keys")String[] keys,@Param(name = "value") Object value) {
        Arrays.stream(keys).forEach(val->putIfAbsent(val,value));
    }

    @Override
    public void putIfAbsent(@Param(name = "key")String key,@Param(name = "value") Object value) {
        if(redisTemplate!=null && Validation.notEmptyAndBlankStr(key) && value !=null){
            redisTemplate.opsForValue().setIfAbsent(key,value);
        }

    }

    @Override
    public void put(@Param(name = "key")String key,@Param(name = "value")Object value,@Param(name = "expireTime") String expireTime) {
        try {
            if(redisTemplate!=null && Validation.notEmptyAndBlankStr(expireTime)&& Validation.notEmptyAndBlankStr(key)  && value !=null){
                Long expire =  Long.valueOf(expireTime);
//                Duration duration =  Duration.ofMillis(expire);
                redisTemplate.opsForValue().set(key,value,expire);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void put(@Param(name = "key")String key,@Param(name = "value")  Object value) {
        if(redisTemplate!=null && Validation.notEmptyAndBlankStr(key) && value !=null){
            redisTemplate.opsForValue().set(key,value);
        }
    }

    @Override
    public Object getValue(@Param(name = "key")String key) {
        if(redisTemplate!=null && Validation.notEmptyAndBlankStr(key)){
            return redisTemplate.opsForValue().get(key);
        }
        return null;
    }

    @Override
    public Object getValue(@Param(name = "keys")String[] keys) {
       List<Object> objectList = Arrays.stream(keys).map(val->getValue(val)).collect(Collectors.toList());
       if(Validation.collectNotEmpty(objectList)){
           return objectList.get(0);
       }
       return null;
    }

    @Override
    public void remove(@Param(name = "key")String key) {
        if(redisTemplate!=null){
            redisTemplate.delete(key);
        }
    }

    @Override
    public void remove(@Param(name = "keys")String[] keys) {
        Arrays.stream(keys).forEach(val->remove(val));
    }
}
