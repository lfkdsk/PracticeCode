package concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 延时锁
 * 锁的申请有时限
 * 如果不加参数就可以直接返回
 * Created by liufengkai on 16/5/13.
 */
public class TimeLock implements Runnable {
    public static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        try {
            if (lock.tryLock(5, TimeUnit.MINUTES)) {
                Thread.sleep(6000);
            } else {
                System.out.println("get lock failed");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        TimeLock lock = new TimeLock();

        Thread thread_1 = new Thread(lock);
        Thread thread_2 = new Thread(lock);

        thread_1.start();
        thread_2.start();
    }
}
