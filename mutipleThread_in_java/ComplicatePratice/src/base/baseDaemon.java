package base;

/**
 * Daemon 守护线程
 * Created by liufengkai on 16/5/13.
 */
public class baseDaemon {
    public static class DaemonThread extends Thread{

        @Override
        public void run() {
            super.run();
            while (true){
                System.out.println("I am alive");
                try {
                    Thread.sleep(1111);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new DaemonThread();
//        t.setDaemon(true);
        t.start();

        Thread.sleep(2000);
    }
}
