package com.mbahrami.todolist.data.models

data class ToDoTask(
    val id:Int,
    val title:String,
    val description:String,
    val priority:Priority
)
