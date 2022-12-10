package com.anqi.distribute.utils.lock.redislock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class DistributedRedisLockAspect {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(DistributedRedisLockAspect.class);

    @Around("@annotation(com.example.pro.utils.lock.DistributedRedisLock)")
    public Object around(ProceedingJoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        com.example.pro.utils.lock.DistributedRedisLock lock = methodSignature.getMethod().getAnnotation(com.example.pro.utils.lock.DistributedRedisLock.class);
        Boolean success;
        if (lock.timeout() == -1) {
            success = redisTemplate.opsForValue().setIfAbsent(lock.key(),"");
        } else {
            success = redisTemplate.opsForValue().setIfAbsent(lock.key(), "", lock.timeout(), TimeUnit.MILLISECONDS);
        }
        Object res = null;
        try {
            if (success != null && success) {
                res = point.proceed();
                if (lock.del()) {
                    redisTemplate.opsForValue().getOperations().delete(lock.key());
                }
            }
        } catch (Throwable throwable) {
            LOG.error("分布式锁执行出错， 错误信息为:", throwable);
        }
        return res;
    }

}
