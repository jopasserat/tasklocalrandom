package fr.isima.random;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jH@CKTheRipper
 * @date 3/4/13
 */
public abstract class RandomSafeRunnable implements Runnable {

    private static final AtomicInteger nextId_ = new AtomicInteger(0);

    private int taskId_;

    private void initTaskId() {
        taskId_ = nextId_.getAndIncrement();
    }

    public RandomSafeRunnable() {
        this.initTaskId();
    }

    public int getTaskId() {
        return taskId_;
    }
}
