package com.mbahrami.todolist.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mbahrami.todolist.ui.screens.list.ListScreen
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action
import kotlinx.serialization.Serializable

@Serializable
data class ListScreenRoute(val action: Action)

fun NavGraphBuilder.listComposable(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    composable<ListScreenRoute>() { navBackStackEntry ->
        val action = navBackStackEntry.toRoute<ListScreenRoute>().action
        ListScreen(
            action = action,
            sharedViewModel = sharedViewModel,
            navigateToTaskScreen = { taskId: Int ->
                navController.navigate(route = TaskScreenRoute(taskId = taskId))
            })
    }
}