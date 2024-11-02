package com.aregyan.compose.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aregyan.compose.database.PrefKeys
import com.aregyan.compose.repository.PrefRepository
import com.aregyan.compose.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author md-azizul-islam
 * Created 10/28/24 at 5:33 PM
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val prefRepository: PrefRepository
) : ViewModel() {
    var uiState by mutableStateOf(LoginState())
        private set

    fun onUserNameChanged(name: String) {
        uiState = if (name.isEmpty()) {
            uiState.copy()
        } else {
            uiState.copy(userName = name)
        }
    }

    fun onPasswordChanged(password: String) {
        uiState = if (password.isEmpty()) {
            uiState.copy()
        } else {
            uiState.copy(password = password)
        }
    }

    fun checkReadWriteValue(){
        viewModelScope.launch {
            prefRepository.writeValue(key=PrefKeys.USER_NAME, value = "Here is the value ${System.currentTimeMillis()}")

            prefRepository.readValue(PrefKeys.USER_NAME, defaultValue = "Blank").onStart {
                Log.e("Value_after","value start")
            }.collect{
                Log.e("Value_after","value $it")
            }
        }
    }
}