package com.mbahrami.todolist.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
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
        enterTransition = {
            slideIntoContainer(
                animationSpec = tween(durationMillis = 500, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Up
            )
        }, exitTransition = {
            slideOutOfContainer(
                animationSpec = tween(durationMillis = 500, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.Down
            )
        },
        arguments = listOf(navArgument(name = TASK_SCREEN_ARG_KEY) {
            type = NavType.IntType
        })
    ) { navBackStackEntry ->
        val taskId = navBackStackEntry.arguments!!.getInt(TASK_SCREEN_ARG_KEY)
        LaunchedEffect(key1 = taskId) {
            sharedViewModel.getSelectedTask(taskId = taskId)
        }
        TaskScreen(
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