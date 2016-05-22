package concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 倒计时器
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/13.
 */
public class CountDownLatchDemo implements Runnable {
    static final CountDownLatch end = new CountDownLatch(10);
    // 需要完成10个项目
    static final CountDownLatchDemo demo = new CountDownLatchDemo();


    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(10) * 1000);
            System.out.println("check complete");
            // 通知完成任务 -1
            end.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            service.submit(demo);
        }

        // 要求主线程等待
        end.await();

        System.out.println("Fire !!!");

        service.shutdown();
    }
}
