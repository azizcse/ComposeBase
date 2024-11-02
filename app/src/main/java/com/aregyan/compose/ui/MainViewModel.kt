package com.aregyan.compose.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aregyan.compose.prefUtil
import com.aregyan.compose.repository.PrefRepository
import com.aregyan.compose.ui.navgraph.Route
import com.aregyan.compose.util.PrefKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(private val prefRepository: PrefRepository) : ViewModel() {
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    var startDestination by mutableStateOf(MainUiState())
        private set

    //private val _startDestination = mutableStateOf(Route.AppStartNavigation.route)
    //val startDestination: State<String> = _startDestination
    fun setNextDestination(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            prefUtil.set(PrefKey.KEY_ONBORDING, false)
            startDestination = startDestination.copy(destinationNav = path)
        }
    }

    init {
        viewModelScope.launch {
            val isOnboardingNeedToSHow = prefUtil.get(PrefKey.KEY_ONBORDING, true)
            isOnboardingNeedToSHow?.let {
                startDestination = if (it) {
                    startDestination.copy(destinationNav = Route.AppStartNavigation.path)
                } else {
                    startDestination.copy(destinationNav = Route.LoginNavigation.path)
                }
            }
            delay(5_000L)
            _loading.emit(false)
        }
    }
}