package com.aregyan.compose.ui.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import retrofit2.Response

/**
 * @author md-azizul-islam
 * Created 11/3/24 at 2:00 PM
 */
class BaseViewModel : ViewModel() {
    val _showProgress = mutableStateOf(false)
    var showProgress: State<Boolean> = _showProgress

    suspend fun <T> callApi(
        showProgress: Boolean = false,
        api: suspend () -> Response<T>
    ): Response<T>? {
        if (showProgress) {
            _showProgress.value = true
        }
        val response = api.invoke()
        if (!response.isSuccessful) {
            _showProgress.value = false
            return null
        }
        _showProgress.value = false
        return response
    }

}