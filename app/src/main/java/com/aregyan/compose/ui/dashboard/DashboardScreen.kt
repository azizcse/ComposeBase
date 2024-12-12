package com.aregyan.compose.ui.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aregyan.compose.R
import com.aregyan.compose.ui.Argument
import com.aregyan.compose.ui.BottomNavigationBar
import com.aregyan.compose.ui.Route
import com.aregyan.compose.ui.details.DetailsScreen
import com.aregyan.compose.ui.login.LoginPage
import com.aregyan.compose.ui.navItems
import com.aregyan.compose.ui.navigateToBottomBarRoute
import com.aregyan.compose.ui.onboarding.OnboardingScreen
import com.aregyan.compose.ui.other.OtherScreen
import com.aregyan.compose.ui.users.UsersScreen
import com.dawinder.btnjc.nav.NavItem
import com.dawinder.btnjc.nav.NavPath
import com.dawinder.btnjc.ui.composables.tabs.HomeScreen
import com.dawinder.btnjc.ui.composables.tabs.ListScreen
import com.dawinder.btnjc.ui.composables.tabs.ProfileScreen
import com.dawinder.btnjc.ui.composables.tabs.SearchScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    drawerState: DrawerState

) {

    TopAppBar(
        title = { Text(currentScreen) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (currentScreen == NavPath.LOGIN.name) {
                return@TopAppBar
            }
            if (!navItems.contains(currentScreen)) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            } else {
                IconButton(onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        }
    )
}


@Composable
fun DashboardScreen() {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: NavPath.HOME.name

    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(250.dp)) {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text(text = "Drawer Item") },
                    selected = false,
                    onClick = {
                        navController.navigate(NavPath.OTHER.name)
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("UserList") },
                    selected = false,
                    onClick = {
                        navController.navigate(NavPath.USERS.name)
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    })
            }
        }) {
        Scaffold(
            topBar = {
                BaseAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    scope = coroutineScope,
                    drawerState = drawerState
                )
            },
            bottomBar = {
                BottomAppBar {
                    BottomNavigationBar(navController = navController)
                }
            },
        ) { innerPadding ->
            NavigationScreen(navController = navController, innerPadding = innerPadding)
        }
    }

}

@Composable
fun NavigationScreen(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController, startDestination = NavItem.Home.path, modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        composable(NavItem.OnBoarding.path) {
            OnboardingScreen {
                navController.navigateToBottomBarRoute(NavItem.Login.path)
            }
        }
        composable(NavItem.Login.path) {
            LoginPage {
                navController.navigateToBottomBarRoute(NavItem.Home.path)
            }
        }
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
        composable(
            route = "${NavItem.UserDetails.path}/{${Argument.USERNAME}}",
            arguments = listOf(
                navArgument(Argument.USERNAME) {
                    type = NavType.StringType
                }
            ),
        ) {
            DetailsScreen()
        }
    }
}
