package base;

/**
 * wait() 和 notify() 是靠一个对象类进行控制的
 * 拿和放都要先获取到object的监视器
 * synchronized(object){
 * <p>
 * }
 * <p>
 * 可以说线程是等待在了某一个对象上面的
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/13.
 */
public class baseWaitAndNotify {
    final static Object object = new Object();

    public static class T1 extends Thread {
        @Override
        public void run() {
            super.run();

            synchronized (object) {
                System.out.println(System.currentTimeMillis() + " T1 start");
                try {
                    System.out.println(System.currentTimeMillis() + " :T1 wait for object");
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + " T1 end");
            }

        }
    }

    public static class T2 extends Thread {
        @Override
        public void run() {
            super.run();
            synchronized (object) {
                System.out.println(System.currentTimeMillis()
                        + "T2 start! notify one thread");
                // notify 被唤醒
                object.notify();
                System.out.println(System.currentTimeMillis()
                        + "T2 end");
                //T1 并不会在notify之后立即启动 而是要等待object锁释放之后
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new T1();
        Thread t2 = new T2();

        t1.start();
        t2.start();
    }
}
