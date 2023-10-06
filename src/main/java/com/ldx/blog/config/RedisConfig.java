package com.ldx.blog.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Uaena
 * @date 2023/5/16 22:27
 */
@Configuration
public class RedisConfig {
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory defaultFactory) {
        return getRedisTemplate(defaultFactory);
    }
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        FastJsonRedisSerializer<Object> valueSerialize = new FastJsonRedisSerializer<>(Object.class);
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(valueSerialize);
        redisTemplate.setHashValueSerializer(valueSerialize);
        return redisTemplate;
    }
}
