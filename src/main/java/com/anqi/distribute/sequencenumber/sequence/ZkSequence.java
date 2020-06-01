package com.anqi.distribute.sequencenumber.sequence;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 用于封装程序在运行时每个表对应的自增器
 * 通过分布式原子自增类（DistributedAtomicLong）实现
 * 每500毫秒重试3次后仍然生成失败则返回null
 */
public class ZkSequence {

    private static final int baseSleepTimeMs = 500;
    private static final int maxRetries = 3;

    private RetryPolicy retryPolicy;
    private DistributedAtomicLong atomicLong;

    public ZkSequence(CuratorFramework client, String counterPath) {
        this.retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        this.atomicLong = new DistributedAtomicLong(client, counterPath, retryPolicy);
    }

    /**
     * 生成自增id
     * @return
     * @throws Exception
     */
    public Long sequence() throws Exception{
        AtomicValue<Long> atomicValue = atomicLong.increment();
        return atomicValue.succeeded()?atomicValue.postValue():null;
    }
}
