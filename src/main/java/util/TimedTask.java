package util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimedTask {

    private final long endTime;
    private final ScheduledFuture<?> future;

    public TimedTask(Runnable task, long delay, TimeUnit unit,
                     ScheduledExecutorService scheduler) {

        long delayMillis = unit.toMillis(delay);
        this.endTime = System.currentTimeMillis() + delayMillis;

        this.future = scheduler.schedule(task, delay, unit);
    }

    public long getTimeRemainingMillis() {
        return Math.max(0, endTime - System.currentTimeMillis());
    }

    public boolean isDone() {
        return future.isDone();
    }

    public void cancel() {
        future.cancel(false);
    }
}