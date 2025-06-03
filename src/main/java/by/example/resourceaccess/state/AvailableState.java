package by.example.resourceaccess.state;

import by.example.resourceaccess.monitor.ResourceMonitor;
import java.util.concurrent.locks.Condition;

public class AvailableState implements ResourceState {
    private final Condition condition;

    public AvailableState(ResourceMonitor monitor) {
        this.condition = monitor.getLock().newCondition();
    }

    @Override
    public void acquire(ResourceMonitor monitor, String name) throws InterruptedException {
        monitor.getLock().lock();
        try {
            while (monitor.getAvailable() == 0) {
                monitor.setState(new UnavailableState()); // Переключение состояния
                monitor.getState().acquire(monitor, name); // Делегируем новому состоянию
                return;
            }
            monitor.setAvailable(monitor.getAvailable() - 1);
            System.out.println(name + " получил ресурс. Осталось: " + monitor.getAvailable());
        } finally {
            monitor.getLock().unlock();
        }
    }

    @Override
    public void release(ResourceMonitor monitor, String name) {
        monitor.getLock().lock();
        try {
            monitor.setAvailable(monitor.getAvailable() + 1);
            System.out.println(name + " освободил ресурс. Доступно: " + monitor.getAvailable());

            // Уведомляем одного ожидающего
            monitor.getCondition().signal();

            if (!(monitor.getState() instanceof AvailableState)) {
                monitor.setState(new AvailableState(monitor));
            }
        } finally {
            monitor.getLock().unlock();
        }
    }
}