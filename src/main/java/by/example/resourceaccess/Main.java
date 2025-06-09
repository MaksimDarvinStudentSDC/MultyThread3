package by.example.resourceaccess;


import by.example.resourceaccess.monitor.ResourceMonitor;
import by.example.resourceaccess.task.ResourceAccessTask;
import by.example.resourceaccess.util.TaskLoadResult;
import by.example.resourceaccess.util.TaskLoader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("D:/MultyThread3/src/main/resources/tasks.txt");

        try {
            TaskLoadResult result = TaskLoader.loadTasks(path);
            List<Callable<String>> tasks = List.copyOf(result.getTasks());

            ExecutorService executor = Executors.newFixedThreadPool(5);
            List<Future<String>> results = executor.invokeAll(tasks);

            for (Future<String> future : results) {
                System.out.println(future.get());
            }

            executor.shutdown();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}