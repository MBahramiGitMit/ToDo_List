package com.mbahrami.todolist.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mbahrami.todolist.ui.screens.task.TaskScreen
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action
import com.mbahrami.todolist.util.Constants.LIST_SCREEN
import com.mbahrami.todolist.util.Constants.TASK_SCREEN
import com.mbahrami.todolist.util.Constants.TASK_SCREEN_ARG_KEY

fun NavGraphBuilder.taskComposable(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = "$TASK_SCREEN/{$TASK_SCREEN_ARG_KEY}",
        arguments = listOf(navArgument(name = TASK_SCREEN_ARG_KEY) {
            type = NavType.IntType
        })
    ) { navBackStackEntry ->
        val taskId = navBackStackEntry.arguments!!.getInt(TASK_SCREEN_ARG_KEY)
        TaskScreen(
            taskId = taskId,
            sharedViewModel = sharedViewModel,
            navigateToListScreen = { action: Action ->
                navController.navigate(route = "${LIST_SCREEN}/${action.name}") {
                    popUpTo(LIST_SCREEN) {
                        inclusive = true
                    }
                }
            })
    }
}