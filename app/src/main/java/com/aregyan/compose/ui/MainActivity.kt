package com.aregyan.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aregyan.compose.ui.theme.BaseAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            BaseAppTheme {
                //val navController = rememberNavController()
                //BottomNavScreen(navController = navController)
                //ComposeApp()
                BaseComposeApp()
                /*Surface(modifier = Modifier.fillMaxSize()) {

                }*/
                /*val navController = rememberNavController()
                //ComposeApp()
                SetupNavGraph(navController);*/
            }
        }
    }
}