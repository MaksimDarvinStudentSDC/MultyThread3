package by.example.resourceaccess.state;

import by.example.resourceaccess.monitor.ResourceMonitor;

public interface ResourceState {
    void acquire(ResourceMonitor monitor, String name) throws InterruptedException;
    void release(ResourceMonitor monitor, String name);
}
