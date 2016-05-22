package base;

/**
 * Suspend() 和 Resume() 是有问题的 会始终持锁
 * 不放资源 而且如果 Resume() 的调用出现了顺序的问题
 * 会产生严重的问题
 * 所以应该使用 wait notify 模拟
 * Created by liufengkai on 16/5/13.
 */
public class baseSuspendResume {
    public static Object object = new Object();

    public static class ChangeObjectThread extends Thread{
        volatile boolean suspendme = false;

        public void suspendMe(){
            suspendme= true;
        }

        public void resumeMe(){
            suspendme = false;
            // 清除中断标记 notify唤醒当前线程
            synchronized (this){
                notify();
            }
        }

        @Override
        public void run() {
            super.run();
            while (true){
                synchronized (this){
                    // 判断自己是不是被挂起了
                    while (suspendme){
                        try {
                            // 如果是就wait()
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // 没被挂起的话就会正常的申请锁进行打印
                synchronized (object){
                    System.out.println("in ChangeObjectThread");
                }

                Thread.yield();
            }
        }
    }

    public static class ReadObjectThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                synchronized (object){
                    System.out.println("in ReadObjectThread");
                }
                Thread.yield();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ChangeObjectThread c1 = new ChangeObjectThread();
        ReadObjectThread r1 = new ReadObjectThread();

        c1.start();
        r1.start();

        Thread.sleep(2000);

        c1.suspendMe();
        System.out.println("suspend c1 2 sec");
        Thread.sleep(2000);
        System.out.println("resume t1");
        c1.resumeMe();
    }
}
