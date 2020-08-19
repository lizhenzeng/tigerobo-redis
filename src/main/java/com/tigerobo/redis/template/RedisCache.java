package com.tigerobo.redis.template;

import com.tigerobo.redis.annotation.Param;
import com.tigerobo.redis.operator.CacheOperator;
import com.tigerobo.redis.utils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.*;
import java.util.stream.Collectors;

public class RedisCache implements CacheOperator {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    public RedisTemplate<String,Object> redisTemplate;

    public RedisCache(Object redisTemplate) {
        if (redisTemplate instanceof RedisTemplate) {
            this.redisTemplate = (RedisTemplate) redisTemplate;
        }
    }
    private static final String LUA_SCRIPT_GET_AND_DELETE =
            "local current = redis.call('get', KEYS[1]);\n" +
                    "if (current) then\n" +
                    "    redis.call('del', KEYS[1]);\n" +
                    "end\n" +
                    "return current;";

    @Override
    public void putIfAbsent(@Param(name = "keys") String[] keys, @Param(name = "value") Object value, @Param(name = "expireTime") String expireTime) {
        Arrays.stream(keys).forEach(val -> putIfAbsent(val, value, expireTime));
    }

    @Override
    public void putIfAbsent(@Param(name = "key") String key, @Param(name = "value") Object value, @Param(name = "expireTime") String expireTime) {
        try {
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
                    logger.info("delete Key is {} success!",key);
                } else {
                    logger.info("delete Key is {} fail!",key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public String clean() {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText("SCRIPT FLUSH");
//        SCRIPT LOAD "return redis.call('SET',KEYS[1],ARGV[1])"
        String has = (String) execute(redisScript, "");
//        map.put(load, has);
        return has;
    }
    public Object execute(RedisScript redisScript, String key, Object... args) {
        return redisTemplate.execute(redisScript, Collections.singletonList(key), args);
    }
    @Override
    public void remove(@Param(name = "keys") String[] keys) {

        Arrays.stream(keys).forEach(val -> remove(val));
    }


}
