package com.example.yeshasprabhakar.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [TodoItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Consider using Kotlin Coroutines Dispatchers.IO for database operations
        // instead of a custom ExecutorService for more idiomatic Kotlin.
        // However, for a direct conversion, we keep the ExecutorService.
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(4)

        fun getDatabase(context: Context): AppDatabase {
            // Double-checked locking
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE
                if (instance != null) {
                    instance
                } else {
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "todo_database"
                    )
                    // Add any migrations or callbacks here if needed in the future
                    .build()
                    .also { INSTANCE = it }
                }
            }
        }
    }
}
