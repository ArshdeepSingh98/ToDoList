package com.example.arshdeep.todolist;

/**
 * Created by Arshdeep on 6/22/2017.
 */

public class Todo{
    int id;
    String Task;
    String Date;
    String Time;
    String Description;

    public Todo(){}

    public Todo(String task, String date) {
        Task = task;
        Date = date;
    }

    public Todo(String task, String date, String time, String description) {
        Task = task;
        Date = date;
        Time = time;
        Description = description;
    }

    public Todo(String task, String date, String description) {
        Task = task;
        Date = date;
        Description = description;
    }

    public Todo(int id, String task, String date, String time, String description) {
        this.id = id;
        Task = task;
        Date = date;
        Time = time;
        Description = description;
    }

    public Todo(int id, String task, String date, String description) {
        this.id = id;
        Task = task;
        Date = date;
        Description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return Task;
    }

    public void setTask(String task) {
        Task = task;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
