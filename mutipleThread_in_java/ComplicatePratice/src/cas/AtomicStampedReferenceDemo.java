package cas;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by liufengkai on 16/5/15.
 */
public class AtomicStampedReferenceDemo {
    static AtomicStampedReference<Integer> money =
            new AtomicStampedReference<>(19, 0);

    public static void main(String[] args) {
        // 模拟多线程为用户充值
        for (int i = 0; i < 3; i++) {
            final int timeStamp = money.getStamp();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        while (true) {
                            Integer m = money.getReference();

                            if (m < 20) {
                                if (money.compareAndSet(m, m + 20, timeStamp, timeStamp + 1)) {
                                    System.out.println("余额 < 20 " + money.getReference());
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }.start();

            new Thread() {

                @Override
                public void run() {
                    super.run();
                    for (int i = 0; i < 100; i++) {
                        while (true) {
                            int timeStamp = money.getStamp();
                            Integer m = money.getReference();
                            if (m > 10) {
                                System.out.println("大于10");
                                if (money.compareAndSet(m, m - 10, timeStamp, timeStamp + 1)) {
                                    System.out.println("成功消费10 余额 " + money.getReference());
                                    break;
                                }
                            } else {
                                System.out.println("没有足够的金额");
                                break;
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }
}
