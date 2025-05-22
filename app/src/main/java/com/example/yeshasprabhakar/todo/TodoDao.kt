package com.example.yeshasprabhakar.todo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {

    @Insert
    fun insert(todoItem: TodoItem)

    @Delete
    fun delete(todoItem: TodoItem)

    @Query("DELETE FROM ToDo_Table WHERE Name = :name AND Date = :date AND Time = :time")
    fun deleteByNameDateTime(name: String, date: String, time: String)

    @Query("SELECT * FROM ToDo_Table ORDER BY ID DESC")
    fun getAllTodos(): LiveData<List<TodoItem>>
}
