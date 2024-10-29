package com.aregyan.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseComposeApp()
                }
                //val navController = rememberNavController()
                //BottomNavScreen(navController = navController)
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