package concurrent;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 循环栅栏
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/13.
 */
public class CyclicBarrierDemo {
    public static class Soldier implements Runnable {

        private String soldier;

        private final CyclicBarrier cyclic;

        public Soldier(CyclicBarrier cyclic, String soldier) {
            this.soldier = soldier;
            this.cyclic = cyclic;
        }

        @Override
        public void run() {
            try {
                // 等待所有子项目 到齐
                cyclic.await();
                doWorks();
                // 等待所有子项目 完成工作 通知下一次计数
                cyclic.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        void doWorks() {
            try {
                Thread.sleep(Math.abs(new Random().nextInt() % 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(soldier + " : 任务完成");
        }
    }

    /**
     *  处理
     */
    public static class BarrierRun implements Runnable {

        boolean flag;

        int N;

        public BarrierRun(boolean flag, int n) {
            this.flag = flag;
            N = n;
        }

        @Override
        public void run() {
            if (flag) {
                System.out.println("order : " + N + " complete");
            } else {
                System.out.println("order : " + N + " assemble");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int N = 10;

        Thread[] allSoldier = new Thread[N];

        boolean flag = false;

        // N = 10 设置循环栅栏计数到10之后 运行BarrierRun 运行
        CyclicBarrier cyclic = new CyclicBarrier(N, new BarrierRun(flag, N));

        // 开始

        System.out.println(" 开始集合 ");

        for (int i = 0; i < N; i++) {
            System.out.println(" 士兵 " + i + " 报道 !");

            allSoldier[i] = new Thread(new Soldier(cyclic, " 士兵 " + i));
            allSoldier[i].start();
        }
    }
}
