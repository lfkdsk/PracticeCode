# ComplitePratice

## Java并发练习

### 锁的一些要点

* 减少锁的持有时间

* 减小锁粒度

```

    ConcurrentHashMap 16 个段 在处理 size()的时候,会拿到所有的锁
    
```

* 锁分离 - 例如读写锁

* 锁粗化

### 无锁 - CAS策略(V,E,N)

### 程序并行化  

* 程序拆解流水线