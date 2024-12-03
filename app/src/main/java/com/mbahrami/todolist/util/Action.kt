package com.mbahrami.todolist.util

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): Action {
    return try {
        if (this.isNullOrEmpty()) Action.NO_ACTION else Action.valueOf(this)
    } catch (e: Exception) {
        Action.NO_ACTION
    }

}