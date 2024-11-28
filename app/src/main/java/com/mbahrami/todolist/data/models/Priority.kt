package com.mbahrami.todolist.data.models

import androidx.compose.ui.graphics.Color
import com.mbahrami.todolist.ui.theme.HighPriorityColor
import com.mbahrami.todolist.ui.theme.LowPriorityColor
import com.mbahrami.todolist.ui.theme.MediumPriorityColor
import com.mbahrami.todolist.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    LOW(LowPriorityColor),
    MEDIUM(MediumPriorityColor),
    HIGH(HighPriorityColor),
    NONE(NonePriorityColor)

}