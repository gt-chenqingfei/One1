package com.oneone.framework.android.schedule;

import android.content.Context;
import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncTaskQueue {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskQueue.class);

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "QueuedAsyncTask #"
                    + this.count.getAndIncrement());
        }

    };

    private final SerialExecutor serialExecutor;

    private final LinkedBlockingDeque<AsyncTask> mLinkedList;

    private final ScheduledExecutorService scheduledExecutor;

    private static final class SerialExecutor implements Executor {

        private final Deque<Runnable> tasks = new LinkedList<Runnable>();

        private Runnable active;

        private final AsyncTaskQueue queue;

        public SerialExecutor(AsyncTaskQueue queue) {
            this.queue = queue;
        }

        public synchronized void execute(final Runnable r) {
            this.tasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } catch (Throwable t) {
                        logger.error(t.getMessage(), t);
                    } finally {
                        scheduleNext();
                    }
                }
            });

            if (this.active == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((this.active = this.tasks.poll()) != null) {
                try {
                    this.queue.scheduledExecutor.execute(this.active);
                } catch (RejectedExecutionException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        protected synchronized void cancelAll(Object object) {
            this.tasks.clear();
        }
    }

    AsyncTaskQueue(Context ctx) {
        this.serialExecutor = new SerialExecutor(this);
        this.mLinkedList = new LinkedBlockingDeque<>();
        this.scheduledExecutor = Executors
                .newSingleThreadScheduledExecutor(THREAD_FACTORY);
    }

    /**
     * Add the specified task to this queue
     *
     * @param task   An {@link AsyncTask}
     * @param params The parameters of the task
     * @return
     */
    public <Params, Progress, Result> AsyncTask<Params, Progress, Result> add(
            AsyncTask<Params, Progress, Result> task, Params... params) {
        this.mLinkedList.add(task);
        return task.executeOnExecutor(this.serialExecutor, params);
    }

    public void cancelAll(Object object) {
        this.serialExecutor.cancelAll(object);
        AsyncTask task;
        if (!this.mLinkedList.isEmpty()) {
            while (null != (task = this.mLinkedList.poll()) && !task.isCancelled()) {
                task.cancel(true);
            }
        }
    }

    public void start() {
        // TODO
    }

    public void stop() {
        this.cancelAll(null);
        this.scheduledExecutor.shutdownNow();
    }

}
