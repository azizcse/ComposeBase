package com.aregyan.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

/**
 * @author md-azizul-islam
 * Created 11/3/24 at 10:28 AM
 */

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}


/*
composable(route="screenA"){
val viewModel:SharedViewModel = it.sharedViewModel(navController=navController)
ScreenA{
 navigateToScreenb={
 viewmode..person = it
 navController.navigate("screenB")
 }
}
}

composable(route="screenB){
val viewModel:SharedViewModel = it.sharedViewModel(navController=navController)
viewModel.person?.let{
ScreenB(it)
}
}
 */