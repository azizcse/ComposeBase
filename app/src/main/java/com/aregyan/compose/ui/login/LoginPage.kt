package com.aregyan.compose.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * @author md-azizul-islam
 * Created 10/28/24 at 12:34 PM
 */

@Composable
fun LoginPage(onSubmit:()->Unit) {
    val ctx = LocalContext.current
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState = viewModel.uiState
    //val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(10.dp)).padding(10.dp), verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            CommonTextField(
                label = "User Name",
                value = uiState.userName,
                onValueChanged = { viewModel.onUserNameChanged(it)}
            )
            CommonTextField(
                label = "Password",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                value = uiState.password,
                onValueChanged = { viewModel.onPasswordChanged(it) }
            )
            Button(
                onClick = {
                    onSubmit()
                    Toast.makeText(ctx,"Name ${uiState.userName} Pass: ${uiState.password}",Toast.LENGTH_LONG).show()
                },
                enabled = uiState.userName.isNotBlank() && uiState.password.isNotBlank(),
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@Composable
fun CommonTextField(
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )
}