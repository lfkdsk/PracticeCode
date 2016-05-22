package pratice;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liufengkai on 16/5/22.
 */
public class shell_sort {
    static int[] shellarr = {1, 22, 333, 44, 54, 0, -123, 44};

    static ExecutorService pool = Executors.newCachedThreadPool();

    public static class ShellSortTask implements Runnable {
        int i = 0;
        int h = 0;

        CountDownLatch l;

        public ShellSortTask(int i, int h, CountDownLatch l) {
            this.i = i;
            this.h = h;
            this.l = l;
        }

        @Override
        public void run() {
            if (shellarr[i] < shellarr[i - h]) {
                int tmp = shellarr[i];
                int j = i - h;
                while (j >= 0 && shellarr[j] > tmp) {
                    shellarr[j + h] = shellarr[j];
                    j -= h;
                }
                shellarr[j + h] = tmp;
            }

            l.countDown();
        }
    }

    public static void pShellSort(int[] arr) throws InterruptedException {
        // 计算出最大的h
        int h = 1;
        CountDownLatch latch = null;
        while (h <= arr.length / 3)
            h = h * 3 + 1;

        while (h > 0) {
            System.out.println(" h :" + h);

            if (h >= 4) {
                latch = new CountDownLatch(arr.length - h);
            }

            for (int i = h; i < arr.length; i++) {
                // 控制线程数量

                if (h >= 4) {
                    pool.execute(new ShellSortTask(i, h, latch));
                } else {
                    if (arr[i] < arr[i - h]) {
                        int tem = arr[i];
                        int j = i - h;
                        while (j >= 0 && arr[j] > tem) {
                            arr[j + h] = arr[j];
                            j -= h;
                        }
                        arr[j + h] = tem;
                    }
                }
            }

            if (latch != null) {
                latch.await();
            }

            h = (h - 1) / 3;

            for (int anArr : arr) {
                System.out.print(anArr + "\t");
            }
            System.out.println();

        }
    }

    public static void main(String[] args) throws InterruptedException {
        pShellSort(shellarr);
    }
    
}
