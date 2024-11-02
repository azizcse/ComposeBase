package com.aregyan.compose.database

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface PrefService {
    suspend fun <T:Any>write(key: Preferences.Key<T>,value:T)

    fun <T:Any> read(key:Preferences.Key<T>,defaultValue: T? = null): Flow<T>
}