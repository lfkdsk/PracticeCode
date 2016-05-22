package base;

/**
 * synchronized 是一个加锁的过程
 * (1) 指定加锁的对象 进入代码的时候要先拿到锁
 * (2) 作用于实例方法 (相当于是对实例方法的加锁)
 * (3) 作用于静态方法 (这里面被加锁的相当于是整个类)
 * synchronized 关键字的位置很重要关系到了是否真的能同步的问题
 * 给出一种错误的加锁方式
 *
 * 其余的错误类似:
 *
 *  synchronized(i){
 *
 *  }
 *  Integer 是不能修改的对象 每次的新赋值生成了新的对象 所以对象更换了
 * Created by liufengkai on 16/5/13.
 */
public class baseSynchronized implements Runnable{
    static int i = 0;
    // 一种错误的加锁方式
    // 对实例方法加锁 加载了整个Runnable上面 没有用
    public synchronized void increase(){
        i++;
    }

    @Override
    public void run() {
        for (int i = 0;i< 10000;i++){
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new baseSynchronized());
        Thread t2 = new Thread(new baseSynchronized());
        t1.start();t2.start();
        t1.join();t2.join();
        System.out.println(" i: " + i);
    }
}
