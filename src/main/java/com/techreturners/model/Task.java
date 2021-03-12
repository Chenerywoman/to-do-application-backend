package com.techreturners.model;

public class Task {

    private String taskId;
    private String description;
    private Boolean completed = false;

    // default constructor to use with object mapper in SaveTasksHandler
    public Task(){};

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
