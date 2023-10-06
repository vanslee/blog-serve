package com.ldx.blog.components;


import com.alibaba.fastjson.JSON;
import com.ldx.blog.constants.RedisKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisDao {

    private static final Logger log = LoggerFactory.getLogger(RedisDao.class.getName());


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public boolean hasKey(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return hasKey != null && hasKey;
    }

    /**
     * 设置长期有效的缓存
     */
//    public void setLTSValue(String key, Object obj) {
//        setValue(key, obj, -1L);
//    }

//    public void setValue(String key, Object obj) {
//        setValue(key, obj, 1800_000L); // 默认缓存30分钟
//    }

    /**
     *
     * @param key 键
     * @param value 值
     * @param milliseconds 过期时间
     */
    public void setValue(String key, String value, long milliseconds) {
        if (milliseconds < 0L) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, milliseconds, TimeUnit.MILLISECONDS);
        }
    }

    public <T> T getValue(Class<T> type, String key, T defaultValue) {
        String str = getString(key);
        if (str == null) {
            return defaultValue;
        }
        try {
            return JSON.parseObject(str, type);
        } catch (Exception e) {
            log.error("【Redis】解析失败:" + str, e);
            return defaultValue;
        }
    }


    public <T> T getValue(Class<T> type, String key) {
        return getValue(type, key, null);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        try {
            long ts = System.currentTimeMillis();
            Object object = redisTemplate.opsForValue().get(key);
            if (object == null) {
                return defaultValue;
            }
            return (String) object;
        } catch (Exception e) {
            log.error("【Redis】", e);
            return defaultValue;
        }
    }

//    public long getLong(String key) {
//        return getLong(key, 0L);
//    }

//    public long getLong(String key, long defaultValue) {
//        String value = getString(key);
//        if (value == null) {
//            return defaultValue;
//        }
//        return NumberUtil.toLong(value, defaultValue);
//    }

//    public int getInt(String key) {
//        return getInt(key, 0);
//    }

//    public int getInt(String key, int defaultValue) {
//        String value = getString(key);
//        if (value == null) {
//            return defaultValue;
//        }
//        return NumberUtil.toInt(value, defaultValue);
//    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }


    private static final long LOCK_DURATION = 300_000L; // 5分

    private String getLockKey(String key) {
        return RedisKeys.LOCK + key;
    }

    /**
     * Redis 锁
     *
     * @param key          关键字
     * @param milliseconds 释放时间（毫秒）
     * @return 锁成功 true；锁失败(已被锁还没释放) false
     */
    public boolean lock(String key, long milliseconds) {
        String redisKey = getLockKey(key);
        String now = String.valueOf(System.currentTimeMillis());
        Boolean setSuccess = redisTemplate.opsForValue().setIfAbsent(redisKey, now, milliseconds, TimeUnit.MILLISECONDS);
        return setSuccess != null && setSuccess;
    }

    public boolean lock(String key) {
        return lock(key, LOCK_DURATION);
    }

    public boolean isLock(String key) {
        String redisKey = getLockKey(key);
        return hasKey(redisKey);
    }

    public void unlock(String key) {
        String redisKey = getLockKey(key);
        redisTemplate.delete(redisKey);
    }


    public Long increment(String key, long milliseconds) {
        Long currNum = redisTemplate.opsForValue().increment(key);
        if(currNum != null && currNum == 1){
            Long expireTime = redisTemplate.getExpire(key);
            if(expireTime == null || expireTime == -1) {
                redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
            }
        }
        return currNum;
    }

    public void removeSetMember(String key, Object... removeValue) {
        redisTemplate.opsForSet().remove(key, removeValue);
    }

    public void addLTSSetMember(String key, Object... value){
        redisTemplate.opsForSet().add(key, value);
    }
    public boolean isLTSMemberExist(String key, Object value){
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    public boolean rmLTSSetMember(String key, Object values) {
        Long rows = redisTemplate.opsForSet().remove(key, values);
        return !Objects.isNull(rows) && rows >= 1;
    }

    /**
     * List操作
     */
    public Long lLPushAll(String key, List<? extends Object> values){
        return redisTemplate.opsForList().leftPushAll(key,values);
    }
    public Long lLPush(String key, Object values){
        return redisTemplate.opsForList().leftPushIfPresent(key,values);
    }



    public Set<Object> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public long getTotalSetMember(String key) {
        Long res = redisTemplate.opsForSet().size(key);
        if (res == null) {
            return 0L;
        }
        return res;
    }
}
