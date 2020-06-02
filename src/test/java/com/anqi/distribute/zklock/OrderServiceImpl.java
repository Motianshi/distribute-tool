package com.anqi.distribute.zklock;

import com.anqi.distribute.lock.Lock;
import com.anqi.distribute.lock.zklock.curator.ZkLockWithCuratorTemplate;

public class OrderServiceImpl {

    private static OrderNumberGenerator generator = new OrderNumberGenerator();
//    Lock lock = new ZkTemplateLock();
//    Lock lock = new ZkSequenTemplateLock();
        Lock lock = new ZkLockWithCuratorTemplate();
    public void createOrder() {
        String orderNum = null;
        try {
            lock.getLock();
            //获取订单号
            orderNum = generator.getNumber();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "---->" + orderNum);


    }

}
