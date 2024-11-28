package com.mbahrami.todolist.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mbahrami.todolist.ui.screens.list.ListScreen
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action
import com.mbahrami.todolist.util.Constants.LIST_SCREEN
import com.mbahrami.todolist.util.Constants.LIST_SCREEN_ARG_KEY
import com.mbahrami.todolist.util.Constants.TASK_SCREEN
import com.mbahrami.todolist.util.toAction

fun NavGraphBuilder.listComposable(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = "${LIST_SCREEN}/{${LIST_SCREEN_ARG_KEY}}",
        arguments = listOf(navArgument(name = LIST_SCREEN_ARG_KEY) {
            type = NavType.StringType
        })
    ) { navBackStackEntry ->
        val action: Action =
            navBackStackEntry.arguments!!.getString(LIST_SCREEN_ARG_KEY).toAction()
        ListScreen(
            action = action,
            sharedViewModel = sharedViewModel,
            navigateToTaskScreen = { taskId: Int ->
                navController.navigate(route = "${TASK_SCREEN}/$taskId")
            })
    }
}