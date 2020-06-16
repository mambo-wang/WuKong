package com.wukong.consumer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
 
import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
 
/**
 * @Description: 分布式锁工具类
 * @Author:
 * @CreateDate: 
 */
@Component
@Slf4j
public class RedisLock {
    @Resource
    private RedisTemplate redisTemplate;
 
    public static final String UNLOCK_LUA;

    private static ThreadLocal<String> LOCK_VALUE = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    private static final String INCR_BY_WITH_TIMEOUT = "local v;" +
            " v = redis.call('incrBy',KEYS[1],ARGV[1]);" +
            "if tonumber(v) == 1 then\n" +
            "    redis.call('expire',KEYS[1],ARGV[2])\n" +
            "end\n" +
            "return v";
 
    /**
     * 释放锁脚本，原子操作
     */
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 获取redis的分布式锁，内部实现使用了redis的setnx。只会尝试一次
     * @param key 要锁定的key
     * @param expireSeconds key的失效时间
     * @return 获得的锁对象（如果为null表示获取锁失败），后续可以调用该对象的unlock方法来释放锁.
     */
    public boolean getLock(final String key, int expireSeconds){
        return getLock(key, expireSeconds, 0, 0);
    }

    /**
     * 获取redis的分布式锁，内部实现使用了redis的setnx。
     * <br/>
     * <span style="color:red;">此方法在获取失败时会自动重试指定的次数,由于多次等待会阻塞当前线程，请尽量避免使用此方法</span>
     *
     * @param key 要锁定的key
     * @param expireSeconds key的失效时间
     * @param maxRetryTimes 最大重试次数,如果获取锁失败，会自动尝试重新获取锁；
     * @param retryIntervalTimeMillis 每次重试之前sleep等待的毫秒数
     * @return 结果
     */
    public boolean getLock(final String key, final int expireSeconds, int maxRetryTimes, long retryIntervalTimeMillis){
        int maxTimes = maxRetryTimes + 1;
        for(int i = 0;i < maxTimes; i++) {
            boolean status = tryLock(key, expireSeconds, TimeUnit.SECONDS);
            if (status) {//抢到锁
                return status;
            }

            if(retryIntervalTimeMillis > 0) {
                try {
                    Thread.sleep(retryIntervalTimeMillis);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if(Thread.currentThread().isInterrupted()){
                break;
            }
        }

        return false;
    }
 
 
    /**
     * 获取分布式锁，原子操作
     * @param lockKey
     * @param expire
     * @param timeUnit
     * @return
     */
    public boolean tryLock(String lockKey, long expire, TimeUnit timeUnit) {
        try{
            RedisCallback<Boolean> callback = (connection) -> {
                return connection.set(lockKey.getBytes(Charset.forName("UTF-8")), LOCK_VALUE.get().getBytes(Charset.forName("UTF-8")), Expiration.seconds(timeUnit.toSeconds(expire)), RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            return (Boolean)redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }
 
    /**
     * 释放锁
     * @param lockKey
     * @return
     */
    public boolean releaseLock(String lockKey) {
        RedisCallback<Boolean> callback = (connection) -> {
            return connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN ,1, lockKey.getBytes(Charset.forName("UTF-8")), LOCK_VALUE.get().getBytes(Charset.forName("UTF-8")));
        };
        return (Boolean)redisTemplate.execute(callback);
    }
 
    /**
     * 获取Redis锁的value值
     * @param lockKey
     * @return
     */
    public String get(String lockKey) {
        try {
            RedisCallback<String> callback = (connection) -> {
                return new String(connection.get(lockKey.getBytes()), Charset.forName("UTF-8"));
            };
            return (String)redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("get redis occurred an exception", e);
        }
        return null;
    }

    /**
     * 计数器,支持设置失效时间，如果key不存在，则调用此方法后计数器为1（本方法使用string序列化方式）
     * @param key
     * @param delta 可以为负数
     * @param timeout 缓存失效时间
     * @param timeUnit 缓存失效时间的单位
     * @return
     */
    public Long incrBy(String key, long delta, long timeout, TimeUnit timeUnit){
        List<String> keys = new ArrayList<>();
        keys.add(key);
        long timeoutSeconds = TimeUnit.SECONDS.convert(timeout, timeUnit);
        String[] args = new String[2];
        args[0] = String.valueOf(delta);
        args[1] = String.valueOf(timeoutSeconds);
        Object currentVal = redisTemplate.execute(new DefaultRedisScript<>(INCR_BY_WITH_TIMEOUT, String.class), keys, args);

        if(currentVal instanceof Long){
            return (Long)currentVal;
        }
        return Long.valueOf((String)currentVal);
    }
 
}