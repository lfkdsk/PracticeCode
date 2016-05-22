package base;

/**
 * join() 等待 yield() 谦让
 * thread.join() 让所在线程等待 目标线程走完再继续走
 * thread.yield() 让出当前的CPU给其他的线程运行
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/13.
 */
public class baseJoinAndYield {
    public volatile static int i = 0;

    public static class AddThread extends Thread {
        @Override
        public void run() {
            super.run();
            for (i = 0; i < 100000; i++) ;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AddThread at = new AddThread();
        at.start();
//        at.join();
        System.out.println(i);
    }
}
