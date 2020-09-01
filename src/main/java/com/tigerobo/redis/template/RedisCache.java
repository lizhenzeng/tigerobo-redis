package com.tigerobo.redis.template;

import com.tigerobo.redis.annotation.Param;
import com.tigerobo.redis.operator.CacheOperator;
import com.tigerobo.redis.utils.ConvertUtils;
import com.tigerobo.redis.utils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RedisCache implements CacheOperator {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);


    private RedisTemplate redisTemplate;

    public RedisCache(Object redisTemplate) {
        if (redisTemplate instanceof RedisTemplate) {
            this.redisTemplate = (RedisTemplate) redisTemplate;
        }
    }

    @Override
    public void increment(@Param(name = "keys") String[] keys, @Param(name = "start") Object start, @Param(name = "stride") Object stride, @Param(name = "ifAbsentNeedIncr") String ifAbsentNeedIncr) {
        Arrays.stream(keys).forEach(val -> increment(val, start, stride, "-1",ifAbsentNeedIncr));
    }

    @Override
    public void increment(@Param(name = "keys") String[] keys, @Param(name = "start") Object start, @Param(name = "stride") Object stride, @Param(name = "expireTime") String expireTime, @Param(name = "ifAbsentNeedIncr") String ifAbsentNeedIncr) {
        Arrays.stream(keys).forEach(val -> increment(val, start, stride, expireTime,ifAbsentNeedIncr));
    }

    @Override
    public Long increment(@Param(name = "key") String key, @Param(name = "start") Object start, @Param(name = "stride") Object stride, @Param(name = "ifAbsentNeedIncr") String ifAbsentNeedIncr) {
        return increment(key, start, stride, "-1", ifAbsentNeedIncr);
    }

    @Override
    public Long increment(@Param(name = "key") String key, @Param(name = "start") Object start, @Param(name = "stride") Object stride, @Param(name = "expireTime") String expireTime, @Param(name = "ifAbsentNeedIncr") String ifAbsentNeedIncr) {
        Long res = null;
        try {
            setRedisTemplateValueSerializer(3);
            if (redisTemplate != null && Validation.notEmptyAndBlankStr(expireTime) && Validation.notEmptyAndBlankStr(key)) {
                if (redisTemplate.opsForValue().setIfAbsent(key, start)) {
                    logger.info("increment {} is absent!", key);
                }
                if(ifAbsentNeedIncr.equalsIgnoreCase("1")){
                    res = redisTemplate.opsForValue().increment(key, ConvertUtils.convertObjectToT(stride, Integer.class).longValue());
                }else{
                    res = ConvertUtils.convertObjectToT(start,Integer.class).longValue();
                }
                if (Validation.notEmptyAndBlankStr(expireTime) && ConvertUtils.convertObjectToT(expireTime, Integer.class) > 0) {
                    logger.info("increment expire time {} !", expireTime);
                    redisTemplate.expire(key, ConvertUtils.convertObjectToT(expireTime, Integer.class), TimeUnit.SECONDS);
                }
                logger.info("increment Key is {} . value is {} success!", key, res);
            }

        } catch (Exception e) {
            logger.error("increment Key is {} . value is {} fail!", key, res);
        }

        return res;
    }


    private void setRedisTemplateValueSerializer(Integer type) {
        switch (type) {
            case 1:
//                redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
                redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
                break;
            case 2:
//                redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
                redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
                break;
            case 3:
                redisTemplate.setValueSerializer(new GenericToStringSerializer(String.class));
                break;
            default:
                redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        }
    }

    @Override
    public void putIfAbsent(@Param(name = "keys") String[] keys, @Param(name = "value") Object value, @Param(name = "expireTime") String expireTime) {
        Arrays.stream(keys).forEach(val -> putIfAbsent(val, value, expireTime));
    }

    @Override
    public void putIfAbsent(@Param(name = "key") String key, @Param(name = "value") Object value, @Param(name = "expireTime") String expireTime) {
        try {
            setRedisTemplateValueSerializer(10);
            if (redisTemplate != null && Validation.notEmptyAndBlankStr(expireTime) && Validation.notEmptyAndBlankStr(key) && value != null) {
                Long expire = Long.valueOf(expireTime);
//                Duration duration =  Duration.ofMillis(expire);
                redisTemplate.opsForValue().setIfAbsent(key.trim(), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void putIfAbsent(@Param(name = "keys") String[] keys, @Param(name = "value") Object value) {
        Arrays.stream(keys).forEach(val -> putIfAbsent(val, value));
    }

    @Override
    public void putIfAbsent(@Param(name = "key") String key, @Param(name = "value") Object value) {
        setRedisTemplateValueSerializer(10);
        if (redisTemplate != null && Validation.notEmptyAndBlankStr(key) && value != null) {
            Boolean putFlag = redisTemplate.opsForValue().setIfAbsent(key.trim(), value);
            if (putFlag) {
                logger.info("put Key is {}. value is {} success!", key, value);
            } else {
                logger.info("put Key is {} . value is {} fail!", key, value);
            }
        }

    }


    @Override
    public void put(@Param(name = "key") String key, @Param(name = "value") Object value, @Param(name = "expireTime") String expireTime) {
        try {
            setRedisTemplateValueSerializer(10);
            if (redisTemplate != null && Validation.notEmptyAndBlankStr(expireTime) && Validation.notEmptyAndBlankStr(key) && value != null) {
                Long expire = Long.valueOf(expireTime);
//                Duration duration =  Duration.ofMillis(expire);
                redisTemplate.opsForValue().set(key.trim(), value, expire);
                value = getValue(key.trim());
                if (value != null) {
                    logger.info("put Key is {}. value is {} success!", key, value);
                } else {
                    logger.info("put Key is {} . value is {} fail!", key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(@Param(name = "key") String key, @Param(name = "value") Object value) {
        if (redisTemplate != null && Validation.notEmptyAndBlankStr(key) && value != null) {
            redisTemplate.opsForValue().set(key.trim(), value);
            value = getValue(key.trim());
            if (value != null) {
                logger.info("put Key is {}. value is {} success!", key, value);
            } else {
                logger.info("put Key is {} . value is {} fail!", key, value);
            }
        }
    }

    @Override
    public Object getValue(@Param(name = "key") String key) {
        setRedisTemplateValueSerializer(10);
        if (redisTemplate != null && Validation.notEmptyAndBlankStr(key)) {
            return redisTemplate.opsForValue().get(key.trim());
        }
        return null;
    }

    @Override
    public Object getValue(@Param(name = "keys") String[] keys) {
        List<Object> objectList = Arrays.stream(keys).map(val -> getValue(val)).collect(Collectors.toList());
        if (Validation.collectNotEmpty(objectList)) {
            return objectList.get(0);
        }
        return null;
    }

    @Override
    public void remove(@Param(name = "key") String key) {
        if (redisTemplate != null) {
            try {

                Boolean deleteFlag = redisTemplate.delete(key.trim());
                if (deleteFlag) {
                    logger.info("delete Key is {} success!", key);
                } else {
                    logger.info("delete Key is {} fail!", key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public Object execute(RedisScript redisScript, String key, Object... args) {
        return redisTemplate.execute(redisScript, Collections.singletonList(key), args);
    }

    @Override
    public void remove(@Param(name = "keys") String[] keys) {

        Arrays.stream(keys).forEach(val -> remove(val));
    }


}
