package com.example.yeshasprabhakar.todo;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository mRepository;
    private LiveData<List<TodoItem>> mAllTodos;

    public TodoViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodos = mRepository.getAllTodos();
    }

    public LiveData<List<TodoItem>> getAllTodos() {
        return mAllTodos;
    }

    public void insert(TodoItem todoItem) {
        mRepository.insert(todoItem);
    }

    public void deleteByNameDateTime(String name, String date, String time) {
        mRepository.deleteByNameDateTime(name, date, time);
    }
}
