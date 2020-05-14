tool-redis 是一款基于注解进行操作redis工具
现在有：
1.@TigeroboEnableCache 启动注解
2.@TigeroboCachePut 存放注解
3.@TigeroboCacheRemove 移除注解
4.@TigeroboCacheEvict 独享注解
使用案例：

@SpringBootApplication
@TigeroboEnableCache
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

}

@TigeroboCacheEvict(keys = {"{#sample.key}+{#sample.value}+{#id}+{#name}","{#sample.key}+{#sample.value}"},expireTime="500000",template = RedisTemplate.class)
public Sample getResponseStr(Long id, String name, Sample sample){
     return sample;
}

 @TigeroboCacheRemove(keys = {"{#sample.key}+{#sample.value}+{#id}+{#name}","{#sample.key}+{#sample.value}","{#sample.key}"},template = RedisTemplate.class)
 public String removeResponseStr(Long id, String name, Sample sample){
      return String.format("id:%s;name:%s",id,name);
}
