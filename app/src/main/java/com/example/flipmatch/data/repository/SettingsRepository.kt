package com.example.flipmatch.data.repository

import com.example.flipmatch.data.DataStoreManager
import com.example.flipmatch.utils.DarkMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepository
    @Inject
    constructor(
        private val dataStoreManager: DataStoreManager,
    ) {
        val darkMode: Flow<DarkMode> = dataStoreManager.darkMode
        val sound: Flow<Boolean> = dataStoreManager.sound
        val notifications: Flow<Boolean> = dataStoreManager.notifications

        suspend fun setDarkMode(mode: DarkMode) {
            dataStoreManager.setDarkMode(mode)
        }

        suspend fun setSound(enabled: Boolean) {
            dataStoreManager.setSound(enabled)
        }

        suspend fun setNotifications(enabled: Boolean) {
            dataStoreManager.setNotifications(enabled)
        }
    }
