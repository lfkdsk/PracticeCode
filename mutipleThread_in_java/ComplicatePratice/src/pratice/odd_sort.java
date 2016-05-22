package pratice;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 冒泡的并行升级
 * 奇偶交替排序
 * ()
 * Created by liufengkai on 16/5/22.
 */
public class odd_sort {
    static int[] arr = {1, 22, 333, 44, 54, 0, -123, 44};

    static int exch_flag = 1;

    static ExecutorService service = Executors.newCachedThreadPool();

    static synchronized void setExch_flag(int v) {
        exch_flag = v;
    }

    static synchronized int getExchFlag() {
        return exch_flag;
    }

    public static class OddEvenSortTask implements Runnable {
        int i;

        CountDownLatch latch;

        public OddEvenSortTask(int i, CountDownLatch latch) {
            this.i = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            if (arr[i] > arr[i + 1]) {
                int temp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = temp;
                setExch_flag(1);
            }
            // 自减
            latch.countDown();

        }
    }


    public static void pOddEvenSort(int[] arr) throws InterruptedException {
        int start = 0;
        while (getExchFlag() == 1 || start == 1) {
            // 进行奇排序
            setExch_flag(0);
            // 偶数的数组长度
            CountDownLatch latch = new CountDownLatch(arr.length / 2
                    - (arr.length % 2 == 0 ? start : 0));
            for (int i = start; i < arr.length - 1; i += 2) {
                service.submit(new OddEvenSortTask(i, latch));
            }

            latch.await();

            for (int anArr : arr) {
                System.out.print(anArr + "\t");
            }
            System.out.println();

            if (start == 0)
                start = 1;
            else
                start = 0;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        pOddEvenSort(arr);

        for (int anArr : arr) {
            System.out.print(anArr + "\t");
        }
    }
}
