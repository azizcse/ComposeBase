package com.aregyan.compose.ui.tabs

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aregyan.compose.ui.Route
import com.aregyan.compose.ui.other.OtherScreen
import com.aregyan.compose.ui.users.UsersScreen
import com.dawinder.btnjc.nav.NavItem
import com.dawinder.btnjc.ui.composables.tabs.HomeScreen
import com.dawinder.btnjc.ui.composables.tabs.ListScreen
import com.dawinder.btnjc.ui.composables.tabs.ProfileScreen
import com.dawinder.btnjc.ui.composables.tabs.SearchScreen

/**
 * Composable function that defines the navigation screens and their corresponding destinations.
 *
 * @param navController The navigation controller used for handling navigation between screens.
 */
@Composable
fun NavigationScreens(navController: NavHostController) {
    NavHost(navController, startDestination = NavItem.Home.path) {
        composable(NavItem.Home.path) { HomeScreen() }
        composable(NavItem.Search.path) { SearchScreen() }
        composable(NavItem.List.path) { ListScreen() }
        composable(NavItem.Profile.path) { ProfileScreen() }
        composable(NavItem.Other.path) { OtherScreen(modifier = Modifier.fillMaxHeight()) }
        composable(NavItem.Users.path) { backStackEntry ->
            UsersScreen(onUserClick = { username ->
                // In order to discard duplicated navigation events, we check the Lifecycle
                if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                    navController.navigate("${Route.DETAIL}/$username")
                }
            })
        }
    }
}