package com.aregyan.compose.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * @author md-azizul-islam
 * Created 10/28/24 at 12:34 PM
 */

@Composable
fun LoginPage() {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        OutlinedTextField(value = userName, onValueChange = { userName = it })
    }
}