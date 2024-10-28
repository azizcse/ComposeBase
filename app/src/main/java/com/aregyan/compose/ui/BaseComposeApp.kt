package com.aregyan.compose.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.aregyan.compose.ui.details.DetailsScreen
import com.aregyan.compose.ui.login.LoginPage
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

/**
 * @author md-azizul-isla
 * Created 10/28/24 at 9:53 AM
 */

val navItems =
    listOf(NavItem.Home.path, NavItem.Search.path, NavItem.List.path, NavItem.Profile.path)

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
fun BaseComposeApp() {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: NavPath.HOME.name

    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                //HorizontalDivider()
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
                if (navItems.contains(currentScreen) && currentScreen != NavPath.LOGIN.name)
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
        navController, startDestination = NavItem.Login.path, modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        composable(NavItem.Login.path) { LoginPage() }
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


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navItems = listOf(NavItem.Home, NavItem.Search, NavItem.List, NavItem.Profile)
    var selectedItem by rememberSaveable { mutableStateOf(0) }
    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    navController.navigate(item.path) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                        selectedItem = index
                    }
                },
            )
        }
    }
}