package com.mbahrami.todolist.util

object Constants {

    const val DATABASE_TABLE = "todo_table"
    const val DATABASE_NAME = "todo_database"

    const val LIST_SCREEN_ARG_KEY = "action"
    const val TASK_SCREEN_ARG_KEY = "task_id"
    const val LIST_SCREEN = "list_screen"
    const val TASK_SCREEN = "task_screen"
    const val LIST_SCREEN_ROUTE = "$LIST_SCREEN/{$LIST_SCREEN_ARG_KEY}"
    const val TASK_SCREEN_ROUTE = "$TASK_SCREEN/{$TASK_SCREEN_ARG_KEY}"

}