package com.aregyan.compose.ui.navgraph

sealed class Route(val path:String) {
    object OnBoardingScreen : Route(path = "onBoardingScreen")

    object HomeScreen : Route(path = "homeScreen")

    object SearchScreen : Route(path = "searchScreen")

    object BookmarkScreen : Route(path = "bookMarkScreen")

    object DetailsScreen : Route(path = "detailsScreen")
    object NewsNavigatorScreen : Route(path = "newsNavigator")
    object LoginNavigation: Route(path = "loginNavigation")
    object LoginScreen:Route(path = "loginScreen")
    object AppStartNavigation : Route(path = "appStartNavigation")

    object DashboardNavigation : Route(path = "dashBoardNavigation")

    object DashboardScreen:Route(path = "dashboardScreen")


}