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
import com.mbahrami.todolist.util.Constants.LIST_SCREEN_ROUTE
import com.mbahrami.todolist.util.Constants.TASK_SCREEN_ARG_KEY
import com.mbahrami.todolist.util.Constants.TASK_SCREEN_ROUTE

fun NavGraphBuilder.taskComposable(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = TASK_SCREEN_ROUTE,
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
                    popUpTo(LIST_SCREEN_ROUTE) {
                        inclusive = true
                    }
                }
            })
    }
}