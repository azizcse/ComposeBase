package com.aregyan.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.aregyan.compose.ui.tabs.BottomNavScreen
import com.aregyan.compose.ui.theme.BaseAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewmodel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseAppTheme {
                val navController = rememberNavController()
                BottomNavScreen(navController = navController)
                //ComposeApp()
                /*Surface(modifier = Modifier.fillMaxSize()) {

                }*/
                /*val navController = rememberNavController()
                //ComposeApp()
                SetupNavGraph(navController);*/
            }
        }
    }
}