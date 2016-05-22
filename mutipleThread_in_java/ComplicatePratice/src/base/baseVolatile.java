package base;

/**
 * volatile 维护了一个可能被多线程访问的变量
 * 也能保证数据的有序和可见性
 * Created by liufengkai on 16/5/13.
 */
public class baseVolatile implements Runnable{
    public static void main(String[] args){
        ThreadGroup group = new ThreadGroup("FuckGroup");
        Thread t1 = new Thread(group,new baseVolatile()," T1 ");
        Thread t2 = new Thread(group,new baseVolatile()," T2 ");
        t1.start();
        t2.start();

        System.out.println(group.activeCount());

        group.list();
    }

    @Override
    public void run() {
        String groupAndName = Thread.currentThread().getThreadGroup().getName()
                + " " + Thread.currentThread().getName();

        while (true){
            System.out.println("i am " + groupAndName);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
