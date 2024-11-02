package com.aregyan.compose.database

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aregyan.compose.util.Constant.USER_SETTINGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PrefServiceImpl(private val context: Context) : PrefService {
    override suspend fun <T : Any> write(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { prefDb ->
            when (value) {
                is String -> prefDb[key] = value
                is Int -> prefDb[key] = value
                is Boolean -> prefDb[key] = value
                is Float -> prefDb[key] = value
                is Long -> prefDb[key] = value
                else -> throw UnsupportedOperationException("Not yet implemented")
            }
        }
    }

    override fun <T : Any> read(key: Preferences.Key<T>, defaultValue: T?): Flow<T> {
        return context.dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        } as Flow<T>
    }
}


private val readOnlyProperty = preferencesDataStore(name = USER_SETTINGS)

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

object PrefKeys {
    val APP_ENTRY = booleanPreferencesKey("keyIntroPage")
    val USER_ID = intPreferencesKey("keyUserId")
    val USER_NAME = stringPreferencesKey("keyUserName")
}