package com.mbahrami.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mbahrami.todolist.data.models.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}