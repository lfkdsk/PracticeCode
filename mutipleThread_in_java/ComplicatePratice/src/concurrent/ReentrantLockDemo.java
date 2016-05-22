package concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁(synchronized)
 * 能完全代替关键字的方法
 * 重入的意思是指一个线程 能够反复拿多次锁
 * Created by liufengkai on 16/5/13.
 */
public class ReentrantLockDemo implements Runnable {
    public static ReentrantLock lock = new ReentrantLock();

    public static int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 10000; j++) {
            lock.lock();
            try {
                i++;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        Thread t1 = new Thread(demo);
        Thread t2 = new Thread(demo);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("i :" + i);
    }
}
