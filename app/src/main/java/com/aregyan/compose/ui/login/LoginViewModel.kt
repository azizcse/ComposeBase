package com.aregyan.compose.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aregyan.compose.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author md-azizul-islam
 * Created 10/28/24 at 5:33 PM
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {
    var uiState by mutableStateOf(LoginState())
        private set

    fun onUserNameChanged(name: String) {
        uiState = if (name.isNullOrEmpty()) {
            uiState.copy()
        } else {
            uiState.copy(userName = name, password = uiState.password)
        }
    }

    fun onPasswordChanged(password: String) {
        uiState = if (password.isNullOrEmpty()) {
            uiState.copy()
        } else {
            uiState.copy(userName = uiState.userName, password = password)
        }
    }
}