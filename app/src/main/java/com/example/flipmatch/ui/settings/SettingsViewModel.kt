package com.example.flipmatch.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipmatch.data.repository.SettingsRepository
import com.example.flipmatch.utils.DarkMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
    ) : ViewModel() {
        val darkMode =
            settingsRepository.darkMode.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                DarkMode.SYSTEM,
            )

        val uiState: StateFlow<SettingsUiState> =
            combine(
                darkMode,
                settingsRepository.sound,
                settingsRepository.notifications,
            ) { darkMode, soundEnabled, notificationsEnabled ->
                SettingsUiState(darkMode, soundEnabled, notificationsEnabled)
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                SettingsUiState(),
            )

        fun setDarkMode(mode: DarkMode) {
            viewModelScope.launch {
                settingsRepository.setDarkMode(mode)
            }
        }

        fun toggleSound(enabled: Boolean) {
            viewModelScope.launch {
                settingsRepository.setSound(enabled)
            }
        }

        fun toggleNotifications(enabled: Boolean) {
            viewModelScope.launch {
                settingsRepository.setNotifications(enabled)
            }
        }
    }

data class SettingsUiState(
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val soundEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
)
