package base;

/**
 * Created by liufengkai on 16/5/13.
 */
public class baseInterrupted {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (true){
                    if (Thread.currentThread().isInterrupted()){
                        System.out.println("interrupted break");
                        break;
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // 这个捕获异常的时候清除了中断标记位
                        e.printStackTrace();

                        System.out.println("interrupted when sleep");

                        // 所以要重新 设置中断状态
                        Thread.currentThread().interrupt();
                    }

                    Thread.yield();
                }
            }
        };

        thread.start();
        //  其实这是在做一个时间差 如果直接中断看不到效果
        Thread.sleep(2000);
        thread.interrupt();
    }
}
