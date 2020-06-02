package com.anqi.distribute.zklock;

import java.util.concurrent.CountDownLatch;

public class TestOrderGenerate1 {

    public static void main(String[] args) {

        final int currency = 30;

        CountDownLatch latch = new CountDownLatch(currency);

        for (int i = 0; i < currency; i++) {
            OrderServiceImpl orderService = new OrderServiceImpl();
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"___我准备好");
                try {
                    latch.countDown();
                    latch.await();

                    orderService.createOrder();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }, "thread-"+i).start();
        }

    }
}
