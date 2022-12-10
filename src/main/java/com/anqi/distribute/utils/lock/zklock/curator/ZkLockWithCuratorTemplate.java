package com.anqi.distribute.utils.lock.zklock.curator;

import com.anqi.distribute.utils.lock.Lock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class ZkLockWithCuratorTemplate implements Lock {
    // zk host地址
    private String host = "localhost";

    // zk自增存储node
    private String lockPath = "/curatorLock";

    // 重试休眠时间
    private static final int SLEEP_TIME_MS = 1000;
    // 最大重试1000次
    private static final int MAX_RETRIES = 1000;
    //会话超时时间
    private static final int SESSION_TIMEOUT = 30 * 1000;
    //连接超时时间
    private static final int CONNECTION_TIMEOUT = 3 * 1000;

    private CuratorFramework curatorFramework;

    InterProcessMutex lock;

   public ZkLockWithCuratorTemplate() {
       curatorFramework = CuratorFrameworkFactory.builder()
               .connectString(host)
               .connectionTimeoutMs(CONNECTION_TIMEOUT)
               .sessionTimeoutMs(SESSION_TIMEOUT)
               .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES))
               .build();
       curatorFramework.start();
       lock = new InterProcessMutex (curatorFramework, lockPath);
    }



    @Override
    public void getLock() throws Exception {
         lock.acquire(5, TimeUnit.SECONDS);
    }

    @Override
    public void unlock() throws Exception {
        lock.release();
    }
}
