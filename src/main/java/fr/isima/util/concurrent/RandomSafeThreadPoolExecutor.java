package fr.isima.util.concurrent;

import java.util.concurrent.*;

public class RandomSafeThreadPoolExecutor extends ThreadPoolExecutor {

//    private final BlockingQueue <RandomSafeRunnable> workQueue;

    public RandomSafeThreadPoolExecutor (int      inCorePoolSize,
                                         int      inMaximumPool,
                                         long     inKeepAliveTime,
                                         TimeUnit inUnit,
                                         BlockingQueue <Runnable> inWorkQueue,
                                         ThreadFactory inThreadFactory,
                                         RejectedExecutionHandler inHandler) {
        super(inCorePoolSize, inMaximumPool, inKeepAliveTime, inUnit, inWorkQueue, inThreadFactory, inHandler);
    }

//    @Override
//    public BlockingQueue<RandomSafeRunnable> getQueue() {
//        return workQueue;
//    }
}
