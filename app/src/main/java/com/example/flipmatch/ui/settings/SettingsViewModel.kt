package com.example.flipmatch.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipmatch.data.repository.SettingsRepository
import com.example.flipmatch.utils.DarkMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
    ) : ViewModel() {
        // StateFlow to observe Dark Mode setting
        val darkMode: StateFlow<DarkMode> =
            settingsRepository.darkMode.stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                DarkMode.SYSTEM,
            )

        // StateFlow to observe Sound setting
        val sound: StateFlow<Boolean> =
            settingsRepository.sound.stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                true,
            )

        // StateFlow to observe Notifications setting
        val notifications: StateFlow<Boolean> =
            settingsRepository.notifications.stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                true,
            )

        // Function to update Dark Mode setting
        fun setDarkMode(mode: DarkMode) {
            viewModelScope.launch {
                settingsRepository.setDarkMode(mode)
            }
        }

        // Function to update Sound setting
        fun toggleSound(enabled: Boolean) {
            viewModelScope.launch {
                settingsRepository.setSound(enabled)
            }
        }

        // Function to update Notifications setting
        fun toggleNotifications(enabled: Boolean) {
            viewModelScope.launch {
                settingsRepository.setNotifications(enabled)
            }
        }
    }
