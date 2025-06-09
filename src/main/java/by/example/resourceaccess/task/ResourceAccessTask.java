package by.example.resourceaccess.task;

import by.example.resourceaccess.monitor.ResourceMonitor;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceAccessTask implements Callable<String> {
    private static final Logger logger = LogManager.getLogger(ResourceAccessTask.class);
    private final String name;
    private final int duration;
    private final ResourceMonitor monitor;

    public ResourceAccessTask(String name, int duration, ResourceMonitor monitor) {
        this.name = name;
        this.duration = duration;
        this.monitor = monitor;
    }

    @Override
    public String call() {
        try {
            logger.info("{} is trying to acquire resource...", name);
            monitor.acquire(name);

            logger.info("{} is using the resource for {} ms", name, duration);
            TimeUnit.MILLISECONDS.sleep(duration);

            monitor.release(name);
            logger.info("{} released the resource", name);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("{} was interrupted", name);
        }
        return name + " finished execution";
    }
}