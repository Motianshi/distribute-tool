package com.anqi.distribute.utils.lock.redislock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedRedisLock {

    /**
     * redis key
     * @return
     */
    String key();

    /**
     * 过期时间 默认60s -1永不过期
     * @return
     */
    long timeout() default 30000;

    /**
     * 执行完成后是否直接删
     * @return
     */
    boolean del() default false;
}
