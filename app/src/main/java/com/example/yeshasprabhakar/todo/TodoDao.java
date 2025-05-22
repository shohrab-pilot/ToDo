package com.example.yeshasprabhakar.todo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert
    void insert(TodoItem todoItem);

    @Delete
    void delete(TodoItem todoItem);

    @Query("DELETE FROM ToDo_Table WHERE Name = :name AND Date = :date AND Time = :time")
    void deleteByNameDateTime(String name, String date, String time);

    @Query("SELECT * FROM ToDo_Table ORDER BY ID DESC")
    LiveData<List<TodoItem>> getAllTodos();
}
