package com.tigerobo.redis.operator;

public interface CacheOperator {

    void putIfAbsent(String[] keys,Object value,String expireTime);

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
