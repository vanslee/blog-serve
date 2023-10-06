package com.ldx.blog.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Uaena
 * @date 2023/5/27 13:11
 */
@Component
public class RedisKeyGenerator implements KeyGenerator {
    /**
     * 自定义缓存的redis的KeyGenerator【key生成策略】
     * 注意: 该方法只是声明了key的生成策略,需在@Cacheable注解中通过keyGenerator属性指定具体的key生成策略
     * 可以根据业务情况，配置多个生成策略
     * 如: @Cacheable(value = "key", keyGenerator = "cacheKeyGenerator")
     */
    @Override
    public Object generate(Object target, Method method, Object... params) {
        // 类名
        String className = target.getClass().getSimpleName();
        // 方法名
        String name = method.getName();
        StringBuilder sb = new StringBuilder(className);
        String customKey = sb.append(":").append(name).append(":").toString();
        return customKey;

    }
}
