package producerAndConstum;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by liufengkai on 16/5/22.
 */
public class Controller {
    public static void main(String[] args) {
        // 共享的缓冲区
        BlockingQueue<Data> queue = new LinkedBlockingDeque<>();

        // 生成的三个生产者
        producer p1 = new producer(queue);

        producer p2 = new producer(queue);

        producer p3 = new producer(queue);

        // 三个消费者

        consumer c1 = new consumer(queue);

        consumer c2 = new consumer(queue);

        consumer c3 = new consumer(queue);

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.execute(p1);
        executorService.execute(p2);
        executorService.execute(p3);

        executorService.execute(c1);
        executorService.execute(c2);
        executorService.execute(c3);

        
    }
}
