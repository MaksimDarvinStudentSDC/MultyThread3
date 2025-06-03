package by.example.resourceaccess.state;


import by.example.resourceaccess.monitor.ResourceMonitor;

public class UnavailableState implements ResourceState {

    @Override
    public void acquire(ResourceMonitor monitor, String name) throws InterruptedException {
        monitor.getLock().lock();
        try {
            while (monitor.getAvailable() == 0) {
                System.out.println(name + " ждёт (ресурсы недоступны)...");
                monitor.getCondition().await(); // Ждём сигнала
            }

            // Когда ресурс появился — переходим обратно в AvailableState и пробуем снова
            monitor.setState(new AvailableState(monitor));
            monitor.getState().acquire(monitor, name);
        } finally {
            monitor.getLock().unlock();
        }
    }

    @Override
    public void release(ResourceMonitor monitor, String name) {
        monitor.getLock().lock();
        try {
            monitor.setAvailable(monitor.getAvailable() + 1);
            System.out.println(name + " попытался освободить ресурс. Доступно: " + monitor.getAvailable());

            // Всегда уведомляем хотя бы один поток
            monitor.getCondition().signal();

            if (monitor.getAvailable() > 0) {
                monitor.setState(new AvailableState(monitor));
            }
        } finally {
            monitor.getLock().unlock();
        }
    }
}