package com.anqi.distribute.utils.lock;

public interface Lock {

    /**
     * 获取锁
     */
    void getLock() throws Exception;

    /**
     * 释放锁
     */
    void unlock() throws Exception;
}
