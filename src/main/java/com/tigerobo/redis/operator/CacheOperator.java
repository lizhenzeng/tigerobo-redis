package com.tigerobo.redis.operator;

import com.tigerobo.redis.annotation.Param;

public interface CacheOperator {

    void increment(@Param(name = "keys") String[] keys, @Param(name = "start") Object start, @Param(name = "stride") Object stride, @Param(name = "ifAbsentNeedIncr") String ifAbsentNeedIncr);

    void increment(@Param(name = "keys") String[] keys, @Param(name = "start") Object start, @Param(name = "stride") Object stride, @Param(name = "expireTime") String expireTime, @Param(name = "ifAbsentNeedIncr") String ifAbsentNeedIncr);

    Long increment(@Param(name = "key") String key, @Param(name = "start") Object start, @Param(name = "stride") Object stride, @Param(name = "ifAbsentNeedIncr") String ifAbsentNeedIncr);

    Long increment(@Param(name = "key") String key, @Param(name = "start") Object start, @Param(name = "stride") Object stride, @Param(name = "expireTime") String expireTime, @Param(name = "ifAbsentNeedIncr") String ifAbsentNeedIncr);

    void putIfAbsent(String[] keys, Object value, String expireTime);

    void putIfAbsent(String key,Object value,String expireTime);

    void putIfAbsent(String[] keys,Object value);

    void putIfAbsent(String key,Object value);

    void put(String key,Object value,String expireTime);

    void put(String key,Object value);

    Object getValue(String key);

    Object getValue(String[] keys);

    void remove(String key);

    void remove(String[] keys);
}
