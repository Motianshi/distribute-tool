# 分布式工具类
+ 根据ip获取城市接口
+ 分布式锁
+ 分布式全局自增id
```
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─anqi
│  │  │          └─distribute
│  │  │              ├─iputil 根据ip获取城市
│  │  │              ├─lock
│  │  │              │  └─zklock
│  │  │              │      ├─curator   curator分布式锁实现
│  │  │              │      ├─zksequen  基于临时顺序节点分布式锁实现
│  │  │              │      └─zksimple  基于临时节点分布式锁实现
│  │  │              └─sequencenumber
│  │  │                  └─sequence  zookeeper 分布式自增id
│  │  └─resources
│  │      └─iputil 根据ip获取城市前端文件
│  └─test
      └─java
          └─com
              └─anqi
                  └─distribute
                     └─zklock 分布式锁测试代码-生成基于时间的订单流水号
```
