package excutor;

import java.util.concurrent.*;

/**
 * Reject Rule 拒绝规则
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/14.
 */
public class RejectThreadPoolDemo {
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

    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask();

        ExecutorService es = new ThreadPoolExecutor(5, 5,
                0L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(10),
                // 线程工厂类
                Executors.defaultThreadFactory(),
                // 拒绝规则
                (r, executor) -> System.out.println(r.toString() + "is discard")) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                // 准备执行
                super.beforeExecute(t, r);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                // 执行结束
                super.afterExecute(r, t);
            }

            @Override
            protected void terminated() {
                super.terminated();
            }
        };

        //  找
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            es.submit(task);
            Thread.sleep(10);
        }
    }
}
