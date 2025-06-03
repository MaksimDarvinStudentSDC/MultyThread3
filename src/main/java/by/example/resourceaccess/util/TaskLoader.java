package by.example.resourceaccess.util;
import by.example.resourceaccess.monitor.ResourceMonitor;
import by.example.resourceaccess.task.ResourceAccessTask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskLoader {

    public static TaskLoadResult loadTasks(Path path) throws Exception {
        int totalResources;
        List<ResourceAccessTask> tasks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            // Первая строка — количество ресурсов
            String firstLine = br.readLine();
            if (firstLine == null || !firstLine.startsWith("totalResources=")) {
                throw new IllegalArgumentException("Файл должен начинаться с totalResources=...");
            }

            totalResources = Integer.parseInt(firstLine.split("=")[1].trim());
            ResourceMonitor monitor = new ResourceMonitor(totalResources);

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.strip().split("\\s+");
                if (parts.length < 2) continue;

                String name = parts[0];
                int duration = Integer.parseInt(parts[1]);

                tasks.add(new ResourceAccessTask(name, duration, monitor));
            }
        }

        return new TaskLoadResult(totalResources, tasks);
    }
}
