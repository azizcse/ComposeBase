package com.aregyan.compose.repository

import androidx.datastore.preferences.core.Preferences
import com.aregyan.compose.database.PrefService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrefRepository @Inject constructor(private val prefService: PrefService) {
    fun <T : Any> readValue(key: Preferences.Key<T>, defaultValue: T? = null): Flow<T> {
        return prefService.read(key = key, defaultValue = defaultValue)
    }

    suspend fun <T : Any> writeValue(key: Preferences.Key<T>, value: T) {
        prefService.write(key = key, value = value)
    }

}