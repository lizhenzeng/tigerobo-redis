package com.tigerobo.redis.template;

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
    public void putIfAbsent(String[] keys, Object value, String expireTime) {
        Arrays.stream(keys).forEach(val->putIfAbsent(val,value,expireTime));
    }

    @Override
    public void putIfAbsent(String key, Object value, String expireTime) {
        try {
            if(redisTemplate!=null && Validation.notEmptyAndBlankStr(expireTime)&& Validation.notEmptyAndBlankStr(key)  && value !=null){
                Long expire =  Long.valueOf(expireTime);
                Duration duration =  Duration.ofMillis(expire);
                redisTemplate.opsForValue().setIfAbsent(key,value,duration);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void putIfAbsent(String[] keys, Object value) {
        Arrays.stream(keys).forEach(val->putIfAbsent(val,value));
    }

    @Override
    public void putIfAbsent(String key, Object value) {
        if(redisTemplate!=null && Validation.notEmptyAndBlankStr(key) && value !=null){
            redisTemplate.opsForValue().setIfAbsent(key,value);
        }

    }

    @Override
    public void put(String key, Object value, String expireTime) {
        try {
            if(redisTemplate!=null && Validation.notEmptyAndBlankStr(expireTime)&& Validation.notEmptyAndBlankStr(key)  && value !=null){
                Long expire =  Long.valueOf(expireTime);
                Duration duration =  Duration.ofMillis(expire);
                redisTemplate.opsForValue().set(key,value,duration);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void put(String key, Object value) {
        if(redisTemplate!=null && Validation.notEmptyAndBlankStr(key) && value !=null){
            redisTemplate.opsForValue().set(key,value);
        }
    }

    @Override
    public Object getValue(String key) {
        if(redisTemplate!=null && Validation.notEmptyAndBlankStr(key)){
            return redisTemplate.opsForValue().get(key);
        }
        return null;
    }

    @Override
    public Object getValue(String[] keys) {
       List<Object> objectList = Arrays.stream(keys).map(val->getValue(val)).collect(Collectors.toList());
       if(Validation.collectNotEmpty(objectList)){
           return objectList.get(0);
       }
       return null;
    }

    @Override
    public void remove(String key) {
        if(redisTemplate!=null){
            redisTemplate.delete(key);
        }
    }

    @Override
    public void remove(String[] keys) {
        Arrays.stream(keys).forEach(val->remove(val));
    }
}
