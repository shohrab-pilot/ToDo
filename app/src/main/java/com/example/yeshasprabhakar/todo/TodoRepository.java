package com.example.yeshasprabhakar.todo;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoRepository {

    private TodoDao mTodoDao;
    private LiveData<List<TodoItem>> mAllTodos;

    public TodoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTodoDao = db.todoDao();
        mAllTodos = mTodoDao.getAllTodos();
    }

    public LiveData<List<TodoItem>> getAllTodos() {
        return mAllTodos;
    }

    public void insert(TodoItem todoItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mTodoDao.insert(todoItem);
        });
    }

    public void deleteByNameDateTime(String name, String date, String time) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mTodoDao.deleteByNameDateTime(name, date, time);
        });
    }
}
