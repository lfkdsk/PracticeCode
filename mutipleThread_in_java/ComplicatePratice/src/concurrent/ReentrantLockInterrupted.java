package concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 中断锁
 * 能响应中断解除锁的等待申请
 * 这是能超过 synchronized 的方式
 * Created by liufengkai on 16/5/13.
 */
public class ReentrantLockInterrupted implements Runnable {
    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    int lock;

    public ReentrantLockInterrupted(int lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            if (lock == 0) {
                // 申请2把锁
                // 获得锁 并且优先响应中断
                lock1.lockInterruptibly();
                Thread.sleep(500);
                lock2.lockInterruptibly();
            } else {
                // 申请
                lock2.lockInterruptibly();
                Thread.sleep(500);
                lock1.lockInterruptibly();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock1.isHeldByCurrentThread())
                lock1.unlock();
            if (lock2.isHeldByCurrentThread())
                lock2.unlock();
            System.out.println(Thread.currentThread().getId() + " 线程退出 ");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockInterrupted r1 = new ReentrantLockInterrupted(1);
        ReentrantLockInterrupted r2 = new ReentrantLockInterrupted(2);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();

        Thread.sleep(1000);
        // 形成死锁 t2 中断了
        t2.interrupt();
    }
}
