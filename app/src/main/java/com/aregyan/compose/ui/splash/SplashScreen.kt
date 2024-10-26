package com.aregyan.compose.ui.splash

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.aregyan.compose.ui.navigation.Screen

@Composable
fun SplashScreen(navHostController: NavHostController) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)) {
        LaunchedEffect(key1 = true) {
            object : CountDownTimer(2000, 200) {
                override fun onTick(millisUntilFinished: Long) {
                    //onNotify()
                }

                override fun onFinish() {
                   //finished = true
                    navHostController.navigate(Screen.Home.route)
                }
            }.start()
        }
    }
}