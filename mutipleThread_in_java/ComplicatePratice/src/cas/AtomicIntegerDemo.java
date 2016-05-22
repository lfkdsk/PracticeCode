package cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS操作实现的Integer
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/15.
 */
public class AtomicIntegerDemo {
    static AtomicInteger integer = new AtomicInteger();

    public static class AddThread implements Runnable {

        @Override
        public void run() {

            for (int k = 0; k < 10000; k++) {
                /**
                 *     public final int getAndAddInt(Object var1, long var2, int var4) {
                             int var5;
                             do {
                                var5 = this.getIntVolatile(var1, var2);
                             } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

                            return var5;
                      }
                 */
                integer.incrementAndGet();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[10];

        for (int i = 0; i < 10; i++) {
            ts[i] = new Thread(new AddThread());
        }

        for (int k = 0; k < 10; k++) ts[k].start();
        for (int k = 0; k < 10; k++) ts[k].join();

        System.out.println(integer);
    }
}
