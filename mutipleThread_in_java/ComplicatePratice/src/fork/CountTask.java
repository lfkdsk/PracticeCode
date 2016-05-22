package fork;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Fork and Join
 * Created by liufengkai on 16/5/14.
 */
public class CountTask extends RecursiveTask<Long> {
    private static final int THRESHOLDD = 10000;

    private long start;

    private long end;

    public CountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long sum = 0;

        boolean canCompute = (end - start) < THRESHOLDD;

        if (canCompute) {
            for (long i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 分成100个小任务

            long step = (start + end) / 100;

            ArrayList<CountTask> subTasks = new ArrayList<>();

            long pos = start;

            for (int i = 0; i < 100; i++) {
                long lastOne = pos + step;

                if (lastOne > end) lastOne = end;

                CountTask subTask = new CountTask(pos, lastOne);

                pos += step + 1;

                subTasks.add(subTask);

                // 分成子任务提交运行
                subTask.fork();

                for (CountTask task : subTasks) {
                    sum += task.join();
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkpool = new ForkJoinPool();

        // 构建任务
        CountTask task = new CountTask(0, 200000L);

        ForkJoinTask<Long> result = forkpool.submit(task);

        try {
            long res = result.get();
            System.out.println("res : " + res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
