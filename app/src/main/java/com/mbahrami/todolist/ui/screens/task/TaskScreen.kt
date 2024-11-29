package com.mbahrami.todolist.ui.screens.task

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (action: Action) -> Unit
) {
    val selectedTask by sharedViewModel.selectedTask.collectAsState()

    Scaffold(
        topBar = {
            TaskAppBar(
                title = selectedTask?.title,
                navigateToListScreen = navigateToListScreen
            )
        },
        content = {
            TaskContent(
                title = sharedViewModel.title.value,
                onTitleChange = { sharedViewModel.title.value = it },
                description = sharedViewModel.description.value,
                onDescriptionChange = { sharedViewModel.description.value = it },
                priority = sharedViewModel.priority.value,
                onPriorityChange = { sharedViewModel.priority.value = it }
            )
        }
    )
}