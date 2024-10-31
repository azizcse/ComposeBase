package com.aregyan.compose.ui.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author md-azizul-islam
 * Created 10/31/24 at 10:14 AM
 */
class PermissionViewModel:ViewModel() {
    private val _permissionState = MutableStateFlow(PermissionState.NO_ACTION)
    val permissionState = _permissionState.asStateFlow()

    private val _events = Channel<Events>()
    val events = _events.receiveAsFlow()

    fun isPermissionGranted(context: Context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            viewModelScope.launch {
                _events.send(Events.ShowGrantedText)
            }
        }
    }

    fun updatePermissionEvent(
        isGranted: Boolean,
        shouldShowPermissionRationale: Boolean
    ) {
        viewModelScope.launch {
            when {
                isGranted -> _events.send(Events.ShowGrantedText)
                shouldShowPermissionRationale -> _events.send(Events.ShowPermissionRationale)
                else -> _events.send(Events.SendToSettings)
            }
        }
    }

    fun updatePermissionState(permissionState: PermissionState) {
        _permissionState.value = permissionState
    }
}

sealed class Events {
    data object ShowGrantedText: Events()
    data object ShowPermissionRationale: Events()
    data object SendToSettings: Events()
}

enum class PermissionState {
    GRANTED,
    SHOULD_SHOW_PERMISSION_RATIONALE,
    NO_ACTION
}