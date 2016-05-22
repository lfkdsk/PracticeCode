package concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁
 * 按顺序拿锁
 * Created by liufengkai on 16/5/13.
 */
public class FairLock implements Runnable {
    public static ReentrantLock lock = new ReentrantLock(true);

    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "获得锁");
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        FairLock r1 = new FairLock();
        Thread t1 = new Thread(r1, " Thread - 1 ");
        Thread t2 = new Thread(r1, " Thread - 2 ");
        t1.start();
        t2.start();
    }
}
