package excutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 固定大小的线程池
 * <p> Nthreads = Ncup * Ucpu * ( 1 + W/C) </p>
 * Created by liufengkai on 16/5/14.
 */
public class fixThreadPoolDemo {
    public static class MyTask implements Runnable {

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " :Thread ID : "
                    + Thread.currentThread().getId());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyTask task = new MyTask();

        ExecutorService es = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            es.submit(task);
        }

    }
}
