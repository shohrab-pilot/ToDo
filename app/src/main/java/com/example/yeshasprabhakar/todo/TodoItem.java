package com.example.yeshasprabhakar.todo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ToDo_Table")
public class TodoItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "Date")
    private String date;

    @ColumnInfo(name = "Time")
    private String time;

    public TodoItem(String name, String date, String time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
