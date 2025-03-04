package com.example.flipmatch.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.flipmatch.utils.DarkMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(
    context: Context,
) {
    private val dataStore = context.dataStore

    companion object {
        private val DARK_MODE_KEY = stringPreferencesKey("dark_mode")
        private val SOUND_KEY = booleanPreferencesKey("sound")
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")
    }

    val darkMode: Flow<DarkMode> =
        dataStore.data.map {
            when (it[DARK_MODE_KEY]) {
                "DARK" -> DarkMode.DARK
                "LIGHT" -> DarkMode.LIGHT
                else -> DarkMode.SYSTEM
            }
        }

    val sound: Flow<Boolean> = dataStore.data.map { it[SOUND_KEY] ?: true }
    val notifications: Flow<Boolean> = dataStore.data.map { it[NOTIFICATIONS_KEY] ?: true }

    suspend fun setDarkMode(mode: DarkMode) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = mode.name
        }
    }

    suspend fun setSound(enabled: Boolean) {
        dataStore.edit { it[SOUND_KEY] = enabled }
    }

    suspend fun setNotifications(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATIONS_KEY] = enabled }
    }
}
