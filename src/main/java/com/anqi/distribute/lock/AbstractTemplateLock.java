package com.anqi.distribute.lock;

public abstract class AbstractTemplateLock implements Lock {
    @Override
    public void getLock() {
        if (tryLock()) {
            System.out.println(Thread.currentThread().getName() + "获取锁成功");
        } else {
            //等待
            waitLock();//事件监听 如果节点被删除则可以重新获取
            //重新获取
            getLock();
        }
    }

    protected abstract void waitLock();

    protected abstract boolean tryLock();

    protected abstract void releaseLock();

    @Override
    public void unlock() {
        releaseLock();
    }
}
