package com.mbahrami.todolist.ui.screens.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mbahrami.todolist.R
import com.mbahrami.todolist.components.DisplayAlertDialog
import com.mbahrami.todolist.data.models.Priority
import com.mbahrami.todolist.data.models.ToDoTask
import com.mbahrami.todolist.ui.screens.task.displayToast
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action
import com.mbahrami.todolist.util.RequestState
import com.mbahrami.todolist.util.SearchAppBarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    action: Action,
    sharedViewModel: SharedViewModel,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val snackBarHostState = remember { SnackbarHostState() }

    var isDeleteAllDialogOpen: Boolean by remember { mutableStateOf(false) }

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchFieldValue by sharedViewModel.searchQuery

    var saveableAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

    LaunchedEffect(key1 = saveableAction) {
        if (action != saveableAction) {
            saveableAction = action
            sharedViewModel.handleAction(action = action)
            displaySnackBar(
                snackBarHostState = snackBarHostState,
                scope = scope,
                actionHandler = { sharedViewModel.handleAction(it) },
                taskTitle = sharedViewModel.title.value,
                action = action
            )
        }
    }

    DisplayAlertDialog(
        isOpen = isDeleteAllDialogOpen,
        title = stringResource(R.string.delete_all_task),
        message = stringResource(R.string.delete_all_task_confirmation),
        onConfirmClicked = {
            sharedViewModel.deleteAllTask()
            displaySnackBar(
                snackBarHostState = snackBarHostState,
                scope = scope,
                actionHandler = { sharedViewModel.handleAction(it) },
                taskTitle = "",
                action = Action.DELETE_ALL
            )
        },
        onCloseClicked = { isDeleteAllDialogOpen = false }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            ListAppBar(
                searchFieldValue = searchFieldValue,
                onSearchFieldValueChanged = { sharedViewModel.onSearchFieldValueChanged(it) },
                searchAppBarState = searchAppBarState,
                onSearchAppBarStateChanged = { sharedViewModel.onSearchAppBarStateChange(newState = it) },
                onSearchClicked = { sharedViewModel.getSearchedTask() },
                onSortClicked = { sharedViewModel.persistSortState(priority = it) },
                onDeleteAllClicked = {
                    if (allTasks is RequestState.Success) {
                        if ((allTasks as RequestState.Success<List<ToDoTask>>).data.isNotEmpty()) {
                            isDeleteAllDialogOpen = true
                        } else {
                            displayToast(context = context, message = "There is no task.")
                        }
                    }
                }

            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        },
        content = { padding ->
            if (sortState is RequestState.Success) {
                ListContent(
                    modifier = Modifier.padding(top = padding.calculateTopPadding()),
                    tasks = when {
                        searchAppBarState == SearchAppBarState.TRIGGERED -> searchedTasks
                        (sortState as RequestState.Success<Priority>).data == Priority.LOW -> lowPriorityTasks
                        (sortState as RequestState.Success<Priority>).data == Priority.HIGH -> highPriorityTasks
                        else -> allTasks
                    },
                    navigateToTaskScreen = navigateToTaskScreen,
                    swipeToDelete = { taskId ->
                        sharedViewModel.swipeToDeleteTask(taskId)
                        displaySnackBar(
                            snackBarHostState = snackBarHostState,
                            scope = scope,
                            actionHandler = { action: Action ->
                                sharedViewModel.handleAction(action)
                            },
                            taskTitle = sharedViewModel.title.value,
                            action = Action.DELETE
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = { onFabClicked(-1) }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button),
        )
    }
}


fun displaySnackBar(
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    actionHandler: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    scope.launch {
        if (action != Action.NO_ACTION) {
            snackBarHostState.currentSnackbarData?.dismiss()
            val snackBarResult = snackBarHostState.showSnackbar(
                message = setMessage(action = action, taskTitle = taskTitle),
                actionLabel = setActionLabel(action = action),
                duration = SnackbarDuration.Short
            )
            undoDeletedTask(
                action = action,
                snackBarResult = snackBarResult,
                actionHandler = actionHandler
            )
        }
    }
}

private fun setMessage(action: Action, taskTitle: String): String {
    return when (action) {
        Action.DELETE_ALL -> "All Tasks Removed!"
        else -> "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action == Action.DELETE)
        "Undo"
    else
        "OK"
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    actionHandler: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        actionHandler(Action.UNDO)

    }
}