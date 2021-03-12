package com.techreturners.model;

public class Task {

    private String taskId;
    private String description;
    private boolean completed;

    // default constructor to use with object mapper in SaveTasksHandler
    public Task(){};

    public Task(String taskId, String description) {
        this.taskId = taskId;
        this.description = description;
        this.completed = false;
    }

    public Task(String taskId, String description, boolean completed) {
        this.taskId = taskId;
        this.description = description;
        this.completed = completed;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
}
