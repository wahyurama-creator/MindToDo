package com.id.mindtodo.navigation.destination


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.id.mindtodo.ui.screen.onboard.OnBoardingScreen
import com.id.mindtodo.ui.util.Constants.ON_BOARD_SCREEN
import com.id.mindtodo.ui.viewmodel.SharedViewModel

@ExperimentalPagerApi
@ExperimentalAnimationApi
fun NavGraphBuilder.onBoardingComposable(
    navigateToListScreen: () -> Unit,
    navController: NavHostController,
) {
    composable(
        route = ON_BOARD_SCREEN
    ) {
        OnBoardingScreen(
            navigateToListScreen = navigateToListScreen,
            navController = navController,
        )
    }
}