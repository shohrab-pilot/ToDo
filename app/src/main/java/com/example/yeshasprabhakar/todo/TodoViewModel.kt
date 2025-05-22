package com.example.yeshasprabhakar.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TodoRepository // Renamed for Kotlin style
    val allTodos: LiveData<List<TodoItem>> // Renamed and made public val

    init {
        repository = TodoRepository(application)
        allTodos = repository.allTodos // Accessing the property directly from Kotlin TodoRepository
    }

    // The getAllTodos() method is removed as allTodos is now a public val.

    fun insert(todoItem: TodoItem) {
        repository.insert(todoItem)
    }

    fun deleteByNameDateTime(name: String, date: String, time: String) {
        repository.deleteByNameDateTime(name, date, time)
    }
}
