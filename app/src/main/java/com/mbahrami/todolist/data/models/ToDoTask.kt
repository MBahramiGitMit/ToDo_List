package com.mbahrami.todolist.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mbahrami.todolist.util.Constants

@Entity(tableName = Constants.DATABASE_TABLE)
data class ToDoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var description: String,
    var priority: Priority
)
