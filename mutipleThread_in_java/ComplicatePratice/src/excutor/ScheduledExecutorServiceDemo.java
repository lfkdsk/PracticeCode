package excutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledExecutorService 任务计划
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/14.
 */
public class ScheduledExecutorServiceDemo {

    private static void scheduleAtFixedRateDemo() {

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);

        // 前面的任务完成 之后才开始调度
        // 任务调度的间隔是 固定的
        ses.scheduleAtFixedRate((Runnable) () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(System.currentTimeMillis() / 1000);
        }, 0, 2, TimeUnit.SECONDS);
    }

    private static void scheduleWithFixDelayDemo() {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
        // 这个运行就是时间间隔加上 完成时间才会开始调度下一个任务
        ses.scheduleWithFixedDelay((Runnable) () -> {
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() / 1000);
        }, 0, 2, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        scheduleWithFixDelayDemo();
    }

}
