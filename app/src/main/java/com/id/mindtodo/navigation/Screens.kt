package com.id.mindtodo.navigation

import androidx.navigation.NavHostController
import com.id.mindtodo.ui.util.Action
import com.id.mindtodo.ui.util.Constants.LIST_SCREEN
import com.id.mindtodo.ui.util.Constants.LIST_SCREEN_NO_ACTION
import com.id.mindtodo.ui.util.Constants.ON_BOARD_SCREEN
import com.id.mindtodo.ui.util.Constants.SPLASH_SCREEN

class Screens(navController: NavHostController, routeDestination: String) {
    val onBoard: () -> Unit = {
        navController.navigate(route = LIST_SCREEN_NO_ACTION) {
            popUpTo(ON_BOARD_SCREEN) { inclusive = true }
        }
    }

    val splash: () -> Unit = {
        navController.navigate(route = routeDestination) { // ON_BOARD_SCREEN
            popUpTo(SPLASH_SCREEN) { inclusive = true }
        }
    }

    val list: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }

    val task: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }
}