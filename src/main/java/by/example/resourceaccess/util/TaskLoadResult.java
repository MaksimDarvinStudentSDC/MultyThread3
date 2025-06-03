package by.example.resourceaccess.util;

import by.example.resourceaccess.task.ResourceAccessTask;
import java.util.List;

public class TaskLoadResult {
    private final int totalResources;
    private final List<ResourceAccessTask> tasks;

    public TaskLoadResult(int totalResources, List<ResourceAccessTask> tasks) {
        this.totalResources = totalResources;
        this.tasks = tasks;
    }

    public int getTotalResources() {
        return totalResources;
    }

    public List<ResourceAccessTask> getTasks() {
        return tasks;
    }
}