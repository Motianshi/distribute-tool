package com.anqi.distribute.job;

import com.example.pro.utils.lock.DistributedRedisLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class ImportJob {


    @Scheduled(fixedRate=5000)
    public void configureTasks() throws InterruptedException {
        Thread.sleep(1000);
        System.err.println(Thread.currentThread().getName()+"-1执行静态定时任务时间: " + LocalDateTime.now());
    }
    @Scheduled(fixedRate=5000)
    public void configureTasks2() throws InterruptedException {
        Thread.sleep(10000);
        System.err.println(Thread.currentThread().getName()+"-2执行静态定时任务时间: " + LocalDateTime.now());
    }
    @Scheduled(fixedRate=5000)
    @DistributedRedisLock(key = "a")
    public void configureTasks3() {
        System.err.println(Thread.currentThread().getName()+"-3执行静态定时任务时间: " + LocalDateTime.now());
    }
}
