package producerAndConstum;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Created by liufengkai on 16/5/22.
 */
public class consumer implements Runnable {
    private BlockingQueue<Data> queue;

    private static final int SLEEPTIME = 1000;

    public consumer(BlockingQueue<Data> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("start consumer id = " + Thread.currentThread().getId());

        Random random = new Random();

        try {
            while (true) {
                // 缓冲区拿到数据
                Data data = queue.take();
                
                if (data != null) {
                    System.out.println("consumer get data");

                    int re = data.getIntData() * data.getIntData();

                    System.out.println(MessageFormat.format("{0}*{1}={2}", data.getIntData(), data.getIntData(), re));

                    Thread.sleep(random.nextInt(SLEEPTIME));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();

            Thread.interrupted();
        }
    }
}
