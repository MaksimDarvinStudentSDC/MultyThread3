package by.example.resourceaccess.monitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceMonitor {
    private int available;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public ResourceMonitor(int total) {
        this.available = total;
    }

    public void acquire(String name) throws InterruptedException {
        lock.lock();
        try {
            while (available == 0) {
                System.out.println(name + " is waiting (resources unavailable)...");
                condition.await();
            }
            available--;
            System.out.println(name + " acquired resource. Remaining: " + available);
        } finally {
            lock.unlock();
        }
    }

    public void release(String name) {
        lock.lock();
        try {
            available++;
            System.out.println(name + " released resource. Available: " + available);
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}