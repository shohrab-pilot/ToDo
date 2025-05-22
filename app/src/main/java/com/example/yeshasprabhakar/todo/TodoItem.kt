package com.example.yeshasprabhakar.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDo_Table")
data class TodoItem(
    @ColumnInfo(name = "Name")
    var name: String,

    @ColumnInfo(name = "Date")
    var date: String,

    @ColumnInfo(name = "Time")
    var time: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // Initialized to 0, Room will auto-generate
}
