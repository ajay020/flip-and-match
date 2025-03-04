package com.example.flipmatch.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipmatch.data.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val dataStoreManager: DataStoreManager,
    ) : ViewModel() {
        val darkMode = dataStoreManager.darkMode.stateIn(viewModelScope, SharingStarted.Lazily, false)
        val sound = dataStoreManager.sound.stateIn(viewModelScope, SharingStarted.Lazily, true)
        val notifications = dataStoreManager.notifications.stateIn(viewModelScope, SharingStarted.Lazily, true)

        fun toggleDarkMode(enabled: Boolean) {
            viewModelScope.launch {
                dataStoreManager.setDarkMode(enabled)
            }
        }

        fun toggleSound(enabled: Boolean) {
            viewModelScope.launch {
                dataStoreManager.setSound(enabled)
            }
        }

        fun toggleNotifications(enabled: Boolean) {
            viewModelScope.launch {
                dataStoreManager.setNotifications(enabled)
            }
        }
    }
