package producerAndConstum;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liufengkai on 16/5/22.
 */
public class producer implements Runnable {

    private volatile boolean isRunning = true;

    private BlockingQueue<Data> queue;

    private static final int SLEEPTIME = 1000;

    private static AtomicInteger count = new AtomicInteger();

    public producer(BlockingQueue<Data> queue) {
        this.queue = queue;
    }

    public void stop() {
        this.isRunning = false;
    }

    @Override
    public void run() {
        Data data = null;
        Random random = new Random();
        try {
            while (isRunning) {
                Thread.sleep(random.nextInt(SLEEPTIME));

                data = new Data(count.incrementAndGet());

                System.out.println("data " + data + " is put in queue");

                // 非阻塞的方式提交数据
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.out.println("failed to put data: " + data);
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();

            Thread.interrupted();
        }

    }
}
