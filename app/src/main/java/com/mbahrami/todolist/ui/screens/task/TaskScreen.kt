package com.mbahrami.todolist.ui.screens.task

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    taskId: Int,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (action: Action) -> Unit
) {
    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = null,
                navigateToListScreen = navigateToListScreen
            )
        },
        content = {}
    )
}