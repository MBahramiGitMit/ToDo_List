package com.mbahrami.todolist.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mbahrami.todolist.R
import com.mbahrami.todolist.components.DisplayAlertDialog
import com.mbahrami.todolist.data.models.Priority
import com.mbahrami.todolist.data.models.ToDoTask
import com.mbahrami.todolist.ui.screens.task.displayToast
import com.mbahrami.todolist.ui.theme.fabBackgroundColor
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action
import com.mbahrami.todolist.util.RequestState
import com.mbahrami.todolist.util.SearchAppBarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListScreen(
    action: Action,
    sharedViewModel: SharedViewModel,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isDeleteAllDialogOpen: Boolean by remember { mutableStateOf(false) }

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchFieldValue by sharedViewModel.searchQuery

    LaunchedEffect(key1 = action) {
        displaySnackBar(
            scaffoldState = scaffoldState,
            scope = scope,
            actionHandler = { sharedViewModel.handleAction(it) },
            taskTitle = sharedViewModel.title.value,
            action = action
        )
    }

    DisplayAlertDialog(
        isOpen = isDeleteAllDialogOpen,
        title = stringResource(R.string.delete_all_task),
        message = stringResource(R.string.delete_all_task_confirmation),
        onConfirmClicked = {
            sharedViewModel.deleteAllTask()
            displaySnackBar(
                scaffoldState = scaffoldState,
                scope = scope,
                actionHandler = { sharedViewModel.handleAction(it) },
                taskTitle = "",
                action = Action.DELETE_ALL
            )
        },
        onCloseClicked = { isDeleteAllDialogOpen = false }
    )

    Scaffold(
        scaffoldState = scaffoldState,
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
        content = {
            if (sortState is RequestState.Success) {
                if (searchAppBarState == SearchAppBarState.TRIGGERED) {
                    ListContent(
                        tasks = searchedTasks,
                        navigateToTaskScreen = navigateToTaskScreen,
                        swipeToDelete = { sharedViewModel.swipeToDeleteTask(it) })
                } else {
                    ListContent(
                        tasks = when ((sortState as RequestState.Success<Priority>).data) {
                            Priority.LOW -> lowPriorityTasks
                            Priority.HIGH -> highPriorityTasks
                            else -> allTasks
                        },
                        navigateToTaskScreen = navigateToTaskScreen,
                        swipeToDelete = { sharedViewModel.swipeToDeleteTask(it) }
                    )
                }
            }
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.fabBackgroundColor,
        onClick = { onFabClicked(-1) }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button),
            tint = Color.White
        )
    }
}


fun displaySnackBar(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    actionHandler: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    scope.launch {
        if (action != Action.NO_ACTION) {
            val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = setMessage(action = action, taskTitle = taskTitle),
                actionLabel = setActionLabel(action = action)
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