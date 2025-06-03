package by.example.resourceaccess.monitor;

import by.example.resourceaccess.state.ResourceState;
import by.example.resourceaccess.state.AvailableState;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceMonitor {
    private int available;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private ResourceState state;

    public ResourceMonitor(int total) {
        this.available = total;
        this.state = new AvailableState(this);
    }

    public int getAvailable() { return available; }
    public void setAvailable(int available) { this.available = available; }

    public Lock getLock() { return lock; }
    public Condition getCondition() { return condition; }

    public void setState(ResourceState state) {
        this.state = state;
    }

    public ResourceState getState() {
        return state;
    }

    public void acquire(String name) throws InterruptedException {
        state.acquire(this, name);
    }

    public void release(String name) {
        state.release(this, name);
    }
}