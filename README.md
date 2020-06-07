# 分布式工具类
+ 分布式锁
```
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─anqi
│  │  │          └─distribute
│  │  │              ├─lock
│  │  │              │  └─zklock
│  │  │              │      ├─curator   curator分布式锁实现
│  │  │              │      ├─zksequen  基于临时顺序节点分布式锁实现
│  │  │              │      └─zksimple  基于临时节点分布式锁实现
│  │  │              └─sequencenumber
│  │  │                  └─sequence  zookeeper 分布式自增id
│  │  └─resources
│  └─test
      └─java
          └─com
              └─anqi
                  └─distribute
                     └─zklock 分布式锁测试代码-生成基于时间的订单流水号
```
