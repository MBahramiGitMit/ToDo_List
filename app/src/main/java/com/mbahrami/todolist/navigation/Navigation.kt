package com.mbahrami.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mbahrami.todolist.navigation.destinations.listComposable
import com.mbahrami.todolist.navigation.destinations.taskComposable
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import com.mbahrami.todolist.util.Action
import com.mbahrami.todolist.util.Constants.LIST_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "${LIST_SCREEN}/${Action.NO_ACTION}"
    ) {
        listComposable(navController = navController, sharedViewModel = sharedViewModel)
        taskComposable(navController = navController, sharedViewModel = sharedViewModel)
    }
}