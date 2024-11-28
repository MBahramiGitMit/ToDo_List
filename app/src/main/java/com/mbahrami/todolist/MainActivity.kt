package com.mbahrami.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mbahrami.todolist.navigation.SetupNavigation
import com.mbahrami.todolist.ui.theme.ToDoListTheme
import com.mbahrami.todolist.ui.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                navController = rememberNavController()
                SetupNavigation(navController = navController, sharedViewModel = sharedViewModel)
            }
        }
    }
}
