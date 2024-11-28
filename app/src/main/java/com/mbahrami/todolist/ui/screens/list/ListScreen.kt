package com.mbahrami.todolist.ui.screens.list

import androidx.compose.runtime.Composable
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action

@Composable
fun ListScreen(
    action: Action,
    sharedViewModel: SharedViewModel,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {

}