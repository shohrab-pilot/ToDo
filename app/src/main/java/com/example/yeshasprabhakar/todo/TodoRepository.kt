package com.example.yeshasprabhakar.todo

import android.app.Application
import androidx.lifecycle.LiveData

class TodoRepository(application: Application) {

    private val todoDao: TodoDao // Renamed for Kotlin style (m-prefix removed)
    val allTodos: LiveData<List<TodoItem>> // Renamed and made public val

    init {
        val db = AppDatabase.getDatabase(application)
        todoDao = db.todoDao()
        allTodos = todoDao.getAllTodos()
    }

    fun insert(todoItem: TodoItem) {
        AppDatabase.databaseWriteExecutor.execute {
            todoDao.insert(todoItem)
        }
    }

    fun deleteByNameDateTime(name: String, date: String, time: String) {
        AppDatabase.databaseWriteExecutor.execute {
            todoDao.deleteByNameDateTime(name, date, time)
        }
    }
}
