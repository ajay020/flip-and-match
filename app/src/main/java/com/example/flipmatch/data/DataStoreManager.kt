package com.example.flipmatch.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(
    context: Context,
) {
    private val dataStore = context.dataStore

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val SOUND_KEY = booleanPreferencesKey("sound")
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")
    }

    val darkMode: Flow<Boolean> = dataStore.data.map { it[DARK_MODE_KEY] ?: false }
    val sound: Flow<Boolean> = dataStore.data.map { it[SOUND_KEY] ?: true }
    val notifications: Flow<Boolean> = dataStore.data.map { it[NOTIFICATIONS_KEY] ?: true }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE_KEY] = enabled }
    }

    suspend fun setSound(enabled: Boolean) {
        dataStore.edit { it[SOUND_KEY] = enabled }
    }

    suspend fun setNotifications(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATIONS_KEY] = enabled }
    }
}
