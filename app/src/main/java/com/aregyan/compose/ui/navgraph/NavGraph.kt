package com.aregyan.compose.ui.navgraph

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.aregyan.compose.ui.MainViewModel
import com.aregyan.compose.ui.dashboard.DashboardScreen
import com.aregyan.compose.ui.login.LoginPage
import com.aregyan.compose.ui.onboarding.OnboardingScreen
import com.aregyan.compose.ui.onboarding.OnboardingViewModel

@Composable
fun NavGraph(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = mainViewModel.startDestination.destinationNav) {
        navigation(
            route = Route.AppStartNavigation.path,
            startDestination = Route.OnBoardingScreen.path,

            ) {
            composable(route = Route.OnBoardingScreen.path) {
                val viewModel = viewModel(modelClass = OnboardingViewModel::class.java)
                OnboardingScreen(onButtonClick = {
                    mainViewModel.setNextDestination(Route.LoginNavigation.path)
                })
            }
        }
        navigation(route = Route.LoginNavigation.path, startDestination = Route.LoginScreen.path) {
            composable(route = Route.LoginScreen.path) {
                LoginPage(onSubmit={
                    mainViewModel.setNextDestination(Route.DashboardNavigation.path)
                })
            }
        }

        navigation(route=Route.DashboardNavigation.path,startDestination=Route.DashboardScreen.path){
            composable(route = Route.DashboardScreen.path) {
                DashboardScreen()
            }
        }
    }

}