package com.id.mindtodo.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.pager.ExperimentalPagerApi
import com.id.mindtodo.navigation.destination.listComposable
import com.id.mindtodo.navigation.destination.onBoardingComposable
import com.id.mindtodo.navigation.destination.splashComposable
import com.id.mindtodo.navigation.destination.taskComposable
import com.id.mindtodo.ui.util.Constants.SPLASH_SCREEN
import com.id.mindtodo.ui.viewmodel.SharedViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    routeDestination: String
) {
    val screen = remember(navController) {
        Screens(
            navController = navController,
            routeDestination = routeDestination
        )
    }

    AnimatedNavHost(navController = navController, startDestination = SPLASH_SCREEN) {
        splashComposable(
            navigateToListScreen = screen.splash
        )
        onBoardingComposable(
            navigateToListScreen = screen.onBoard,
            navController = navController,
        )
        listComposable(
            navigateToTaskScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            navigateToListScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
    }
}