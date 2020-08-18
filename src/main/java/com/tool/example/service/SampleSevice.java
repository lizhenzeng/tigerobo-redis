package com.tool.example.service;

import com.tigerobo.redis.annotation.Param;
import com.tigerobo.redis.annotation.TigeroboCacheEvict;
import com.tigerobo.redis.annotation.TigeroboCacheRemove;
import com.tool.example.entity.Sample;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SampleSevice {
    @TigeroboCacheEvict(keys = {"{#sample.key}+{#sample.value}+{#id}+{#name}","{#sample.key}+{#sample.value}"},expireTime="500000",template = RedisTemplate.class)
    public Sample getResponseStr(@Param(name="id")Long id,@Param(name="name")String name, @Param(name="sample")Sample sample){
        return sample;
    }

    @TigeroboCacheRemove(keys = {"{#sample.key}+{#sample.value}+{#id}+{#name}","{#sample.key}+{#sample.value}","{#sample.key}"},template = RedisTemplate.class)
    public String removeResponseStr(@Param(name="id")Long id, @Param(name="name")String name, @Param(name="sample")Sample sample){
        return String.format("id:%s;name:%s",id,name);
    }
}
