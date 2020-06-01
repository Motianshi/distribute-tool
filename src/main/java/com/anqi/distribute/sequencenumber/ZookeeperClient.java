package com.anqi.distribute.sequencenumber;


import com.anqi.distribute.sequencenumber.sequence.ZkSequence;
import com.anqi.distribute.sequencenumber.sequence.ZkSequenceEnum;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;

public class ZookeeperClient {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperClient.class);

    // zk host地址
    private String host;

    // zk自增存储node
    private String sequencePath;

    // 重试休眠时间
    private static final int SLEEP_TIME_MS = 1000;
    // 最大重试1000次
    private static final int MAX_RETRIES = 1000;
    //会话超时时间
    private static final int SESSION_TIMEOUT = 30 * 1000;
    //连接超时时间
    private static final int CONNECTION_TIMEOUT = 3 * 1000;

    //客户端
    private CuratorFramework curatorFramework;

    private Map<String, ZkSequence> zkMap = Maps.newConcurrentMap();

    public ZookeeperClient(String host, String sequencePath) {
        this.host = host;
        this.sequencePath = sequencePath;
    }

    @PostConstruct
    public void init() {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(host)
                .connectionTimeoutMs(CONNECTION_TIMEOUT)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES))
                .build();
        curatorFramework.start();
        initSequence();
    }

    public void initSequence(){
        ZkSequenceEnum[] enums = ZkSequenceEnum.values();
        for (ZkSequenceEnum seq : enums) {
            //拼接各个表的node节点path
            String name = seq.name();
            String path = this.sequencePath + name;
            ZkSequence zkSequence = new ZkSequence(curatorFramework, path);
            zkMap.put(name, zkSequence);
        }
    }

    public Long sequence(ZkSequenceEnum tableName) throws Exception {
        ZkSequence zkSequence = zkMap.get(tableName.name());
        if (null != zkSequence) {
            return zkSequence.sequence();
        } else {
            return null;
        }
    }
}
