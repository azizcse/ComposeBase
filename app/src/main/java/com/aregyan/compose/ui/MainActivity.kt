package com.aregyan.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.aregyan.compose.ui.tabs.BottomNavScreen
import com.aregyan.compose.ui.theme.BaseAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewmodel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewmodel.loading.value
            }
        }
        setContent {
            BaseAppTheme {
                val navController = rememberNavController()
                BottomNavScreen(navController = navController)
                /*Surface(modifier = Modifier.fillMaxSize()) {

                }*/
                /*val navController = rememberNavController()
                //ComposeApp()
                SetupNavGraph(navController);*/
            }
        }
    }
}