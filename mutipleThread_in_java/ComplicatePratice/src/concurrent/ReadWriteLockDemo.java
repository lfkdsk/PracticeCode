package concurrent;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * 解决了读操作之间的阻塞 \ 读操作一般不破坏数据 不需要阻塞
 * 读写之间是并性操作 \ 写写之间是串行
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/13.
 */
public class ReadWriteLockDemo {

    private static Lock lock = new ReentrantLock();

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    private static Lock readLock = reentrantReadWriteLock.readLock();

    private static Lock writeLock = reentrantReadWriteLock.writeLock();

    private int value;


    public Object handleRead(Lock lock) {
        try {
            lock.lock();                // read
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return value;
    }

    public void handleWrite(Lock lock, int index) {
        try {
            lock.lock();        // write
            Thread.sleep(1000);
            value = index;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final ReadWriteLockDemo demo = new ReadWriteLockDemo();


        // 读线程
        Runnable ReadRunnable = () -> {
            demo.handleRead(readLock);
//            demo.handleRead(lock);
        };

        // 写线程
        Runnable WriteRunnable = () -> {
            demo.handleWrite(writeLock, new Random().nextInt());
//            demo.handleWrite(lock, new Random().nextInt());
        };


        for (int i = 0; i < 18; i++) {
            new Thread(ReadRunnable).start();
        }

        for (int i = 18; i < 20; i++) {
            new Thread(WriteRunnable).start();
        }

    }
}
