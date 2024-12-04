package com.mbahrami.todolist.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mbahrami.todolist.R
import com.mbahrami.todolist.components.DisplayAlertDialog
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action


@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (action: Action) -> Unit
) {
    val selectedTask by sharedViewModel.selectedTask.collectAsState()

    var isDeleteDialogOpen: Boolean by remember { mutableStateOf(false) }

    val title by sharedViewModel.title
    val description by sharedViewModel.description
    val priority by sharedViewModel.priority

    val context = LocalContext.current

    BackHandler(enabled = true, onBack = {
        navigateToListScreen(Action.NO_ACTION)
    })

    selectedTask?.let {
        DisplayAlertDialog(
            isOpen = isDeleteDialogOpen,
            title = stringResource(R.string.delete_task, selectedTask!!.title),
            message = stringResource(R.string.delete_task_confirmation, selectedTask!!.title),
            onConfirmClicked = {
                navigateToListScreen(Action.DELETE)
            },
            onCloseClicked = { isDeleteDialogOpen = false }
        )
    }

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
                    } else if (action == Action.DELETE) {
                        isDeleteDialogOpen = true
                    } else {
                        navigateToListScreen(action)
                    }

                }
            )
        },
        content = { padding ->
            TaskContent(
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
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
