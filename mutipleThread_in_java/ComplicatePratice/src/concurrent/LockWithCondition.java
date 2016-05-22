package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition 相当于是rp操作中的sync关键字
 * 包裹对象\方法\类这个样子的东西
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/13.
 */
public class LockWithCondition implements Runnable {
    public static ReentrantLock lock = new ReentrantLock();

    public static Condition condition = lock.newCondition();

    @Override
    public void run() {
        try {
            lock.lock();
            // 此处等待
            condition.await();
            System.out.println("The Thread is going on.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockWithCondition l1 = new LockWithCondition();

        Thread t1 = new Thread(l1);

        t1.start();

        Thread.sleep(2000);

        // 通知线程 t1 继续运行
        lock.lock();
        condition.signal();
        // 唤醒也要给锁 否则无法运行
        lock.unlock();
    }
}
