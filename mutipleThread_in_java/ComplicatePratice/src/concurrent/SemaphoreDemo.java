package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 信号量
 * 锁的一种扩展
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/13.
 */
public class SemaphoreDemo implements Runnable {
    final Semaphore semaphore = new Semaphore(5);


    @Override
    public void run() {
        try {
            semaphore.acquire();
            // sleep
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getId() + " : done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        // 20 大小的线程池
        ExecutorService exec = Executors.newFixedThreadPool(20);

        final SemaphoreDemo demo = new SemaphoreDemo();
        for (int i = 0; i < 20; i++) {
            exec.submit(demo);
        }
    }

}
