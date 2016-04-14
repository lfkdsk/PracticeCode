# MutipleThread in Java

## some tips:

* stop thread:  
  不要使用 thread.stop(); 直接放弃权限立即释放锁引起的内容错误。
  使用标记量跳出死循环。
* interrupt：  
  处理中断标记

* wait and notify:  
  等待、唤醒  
  warning：结合synchronized使用两种操作都要获取对象锁。

* suspend & resume:  
  warning:不推荐用法  
  使用wait & notify实现

* join & yield:  
  等待结束、谦让  
  join使调用线程停在调用的线程的对象上。

* volatile:
    // 同时修改产生冲突
  多线程同步

  ThreadGroup:线程组

  DaemonThread:守护线程

  priority:[1-10]

### 重入锁、公平锁、Condition

### 信号量、倒数器、CyclicBarrier（循环栅栏）
