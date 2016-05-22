package concurrent;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport
 * 安全的挂起\恢复操作
 * Created by liuengkai on 16/5/14.
 */
public class LockSupportDemo {
    public static Object u = new Object();

    final static ChangeObjectThread t1 = new ChangeObjectThread("t-1");

    final static ChangeObjectThread t2 = new ChangeObjectThread("t-2");

    public static class ChangeObjectThread extends Thread {
        public ChangeObjectThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            super.run();
            synchronized (u) {
                System.out.println("in " + getName());
                LockSupport.park();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        t1.start();
        Thread.sleep(1000);
        t2.start();

        LockSupport.unpark(t1);
        LockSupport.unpark(t2);

        t1.join();
        t2.join();
    }
}
