package excutor;

import java.util.concurrent.*;

/**
 * 能够打印异常堆栈的线程池
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/14.
 */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {

    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, clientTrace(), Thread.currentThread().getName()));
    }

    private Runnable wrap(final Runnable task,
                          final Exception clientStack,
                          String threadName) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                System.out.println(threadName);
                clientStack.printStackTrace();
                throw e;
            }
        };
    }

    private Exception clientTrace() {
        return new Exception("Client stack trace");
    }


}
