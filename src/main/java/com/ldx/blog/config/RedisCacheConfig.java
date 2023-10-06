package com.ldx.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Uaena
 * @date 2023/5/16 22:32
 */
@Configuration
public class RedisCacheConfig {

    /**
     * $ {cache} 获取配置文件的配置信息   #{}是spring表达式，获取Bean对象的属性
     */
    @Value("#{${cache.ttl}}")
    private Map<String, Long> ttlParams;

    /**
     * 此方法不能用@Ben注解，避免替换Spring容器中的同类型对象
     */
    public GenericJackson2JsonRedisSerializer serializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration
                // 设置key采用String的序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                //设置value序列化方式采用jackson方式序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer()))
                //当value为null时不进行缓存
                .disableCachingNullValues()
                //cacheNames后面变单引号
                .computePrefixWith(name->name + ":")
                // 配置缓存空间名称的前缀
//                .prefixCacheNameWith("blog:article:")
                //全局配置缓存过期时间【可以不配置】
                .entryTtl(Duration.ofMinutes(30L));
        //专门指定某些缓存空间的配置，如果过期时间【主要这里的key为缓存空间名称】
        Map<String, RedisCacheConfiguration> map = new HashMap<>(3);
        Set<Map.Entry<String, Long>> entries = ttlParams.entrySet();
        for (Map.Entry<String, Long> entry : entries) {
            //指定特定缓存空间对应的过期时间
            map.put("user", redisCacheConfiguration.entryTtl(Duration.ofSeconds(40)));
            map.put(entry.getKey(), redisCacheConfiguration.entryTtl(Duration.ofSeconds(entry.getValue())));
        }
        return RedisCacheManager
                .builder(redisConnectionFactory)
                //默认配置
                .cacheDefaults(redisCacheConfiguration)
                //某些缓存空间的特定配置
                .withInitialCacheConfigurations(map)
                .build();
    }


}
