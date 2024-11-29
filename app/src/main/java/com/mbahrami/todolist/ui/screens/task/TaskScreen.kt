package com.mbahrami.todolist.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (action: Action) -> Unit
) {
    val selectedTask by sharedViewModel.selectedTask.collectAsState()

    val title by sharedViewModel.title
    val description by sharedViewModel.description
    val priority by sharedViewModel.priority

    val context = LocalContext.current

    BackHandler(enabled = true, onBack = {
        navigateToListScreen(Action.NO_ACTION)
    })

    Scaffold(
        topBar = {
            TaskAppBar(
                title = selectedTask?.title,
                navigateToListScreen = { action: Action ->
                    if (action == Action.ADD || action == Action.UPDATE) {
                        if (!sharedViewModel.validateFields()) {
                            displayToast(context = context, message = "Fields Empty!")
                        } else {
                            navigateToListScreen(action)
                        }
                    } else {
                        navigateToListScreen(action)
                    }

                }
            )
        },
        content = {
            TaskContent(
                title = title,
                onTitleChange = { sharedViewModel.onTitleChanged(newValue = it) },
                description = description,
                onDescriptionChange = { sharedViewModel.description.value = it },
                priority = priority,
                onPriorityChange = { sharedViewModel.priority.value = it }
            )
        }
    )
}

fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
