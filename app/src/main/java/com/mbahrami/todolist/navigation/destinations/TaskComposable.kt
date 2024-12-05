package com.mbahrami.todolist.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mbahrami.todolist.ui.screens.task.TaskScreen
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action
import kotlinx.serialization.Serializable

@Serializable
data class TaskScreenRoute(val taskId: Int)

fun NavGraphBuilder.taskComposable(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    composable<TaskScreenRoute>(
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
        }
    ) { navBackStackEntry ->
        val taskId: Int = navBackStackEntry.toRoute<TaskScreenRoute>().taskId
        LaunchedEffect(key1 = taskId) {
            sharedViewModel.getSelectedTask(taskId = taskId)
        }
        TaskScreen(
            sharedViewModel = sharedViewModel,
            navigateToListScreen = { action: Action ->
                navController.navigate(route = ListScreenRoute(action = action)) {
                    navController.popBackStack<ListScreenRoute>(inclusive = true)
                }
            })
    }
}