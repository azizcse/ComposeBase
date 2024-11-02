package com.aregyan.compose.ui

import com.aregyan.compose.ui.navgraph.Route

data class MainUiState(val destinationNav:String = Route.AppStartNavigation.path)