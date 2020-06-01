package com.anqi.distribute.lock.zklock.zksequen;


import com.anqi.distribute.lock.AbstractTemplateLock;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZkSequenTemplateLock extends AbstractTemplateLock {
    private static final String zkServers = "127.0.0.1:2181";
    private static final int sessionTimeout = 8000;
    private static final int connectionTimeout = 5000;

    private static final String lockPath = "/lockPath11";

    private String beforePath;
    private String currentPath;


    private ZkClient client;

    public ZkSequenTemplateLock() {
        client = new ZkClient(zkServers);
        if (!client.exists(lockPath)) {
            client.createPersistent(lockPath);

        }
        log.info("zk client 连接成功:{}",zkServers);

    }



    @Override
    protected void waitLock() {
        CountDownLatch latch = new CountDownLatch(1);
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("监听到节点被删除");
                latch.countDown();
            }
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {}
        };
        //给排在前面的节点增加数据删除的watcher，本质是启动另一个线程去监听上一个节点
        client.subscribeDataChanges(beforePath, listener);

        //阻塞自己
        if (client.exists(beforePath)) {
            try {
                System.out.println("阻塞"+currentPath);
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //取消watcher注册
        client.unsubscribeDataChanges(beforePath, listener);
    }

    @Override
    protected boolean tryLock() {
        if (currentPath == null) {
            //创建一个临时顺序节点
            currentPath = client.createEphemeralSequential(lockPath + "/", "lock-data");
            System.out.println("current:" + currentPath);
        }

        //获得所有的子节点并排序。临时节点名称为自增长的字符串
        List<String> childrens = client.getChildren(lockPath);
        //排序list，按自然顺序排序
        Collections.sort(childrens);
        System.out.println(currentPath+"--"+childrens.get(0));

        if (currentPath.equals("/lockPath9/0000000000")){
            System.out.println("^^^"+beforePath+"^^"+childrens);
        }

        if (currentPath.equals(lockPath + "/" + childrens.get(0))) {
            System.out.println(Thread.currentThread().getName() + "获取到了锁");
            return true;
        } else {
            //如果当前节点不是排第一，则获取前面一个节点信息，赋值给beforePath
            int curIndex = childrens.indexOf(currentPath.substring(lockPath.length() + 1));
            beforePath = lockPath + "/" + childrens.get(curIndex - 1);
        }
        System.out.println("beforePath"+beforePath);
        return false;
    }



    @Override
    public void releaseLock() {
        System.out.println("delete:" + currentPath);
        client.delete(currentPath);
    }
}
