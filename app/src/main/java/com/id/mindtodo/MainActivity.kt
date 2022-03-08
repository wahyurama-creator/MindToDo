package com.id.mindtodo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.id.mindtodo.navigation.SetupNavigation
import com.id.mindtodo.ui.theme.MindToDoTheme
import com.id.mindtodo.ui.viewmodel.SharedViewModel
import com.id.mindtodo.ui.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalPagerApi
@AndroidEntryPoint
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel
    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MindToDoTheme {
                val routeDestination by splashViewModel.startDestination
                navController = rememberAnimatedNavController()
                SetupNavigation(
                    navController = navController,
                    sharedViewModel = sharedViewModel,
                    routeDestination = routeDestination
                )
                Log.e("ROUTE_DESTINATION", routeDestination)
            }
        }
    }
}