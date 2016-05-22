package framework;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liufengkai on 16/5/15.
 */
public class ThreadLocalDemo {
    private static final ThreadLocal<SimpleDateFormat> sdf =
            new ThreadLocal<>();

    public static class ParseDate implements Runnable {
        int i = 0;

        public ParseDate(int i) {
            this.i = i;
        }

        @Override
        public void run() {

            try {
                // 对象的分配其实是在应用层次完成的
                // ThreadLocal 只负责提供容器
                if (sdf.get() == null){
                    sdf.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                }

                Date t = sdf.get().parse("2015-03-29 19:29:" + i % 60);

                System.out.println("I : " + t);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            es.submit(new ParseDate(i));
        }
    }
}
