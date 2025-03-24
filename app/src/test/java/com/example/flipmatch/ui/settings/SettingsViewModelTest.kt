package com.example.flipmatch.ui.settings

import app.cash.turbine.test
import com.example.flipmatch.MainDispatcherRule
import com.example.flipmatch.data.repository.SettingsRepository
import com.example.flipmatch.utils.DarkMode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockSettingsRepository: SettingsRepository = mockk(relaxed = true)
    private lateinit var viewModel: SettingsViewModel

    private val darkModeFlow = MutableStateFlow(DarkMode.SYSTEM)
    private val soundFlow = MutableStateFlow(true)
    private val notificationsFlow = MutableStateFlow(true)

    @Before
    fun setUp() {
        // Mock repository flows
        coEvery { mockSettingsRepository.darkMode } returns darkModeFlow
        coEvery { mockSettingsRepository.sound } returns soundFlow
        coEvery { mockSettingsRepository.notifications } returns notificationsFlow

        // Mock repository methods
        coEvery { mockSettingsRepository.setDarkMode(any()) } returns Unit
        coEvery { mockSettingsRepository.setSound(any()) } returns Unit
        coEvery { mockSettingsRepository.setNotifications(any()) } returns Unit

        // Initialize ViewModel
        viewModel = SettingsViewModel(mockSettingsRepository)
    }

    @Test
    fun `darkMode flow should emit initial value`() =
        runTest {
            viewModel.darkMode.test {
                assertEquals(DarkMode.SYSTEM, awaitItem())
            }
        }

    @Test
    fun `uiState should emit correct initial values`() =
        runTest {
            viewModel.uiState.test {
                assertEquals(SettingsUiState(DarkMode.SYSTEM, true, true), awaitItem())
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setDarkMode should call repository`() =
        runTest {
            viewModel.setDarkMode(DarkMode.DARK)

            advanceUntilIdle()

            coVerify { mockSettingsRepository.setDarkMode(DarkMode.DARK) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `toggleSound should call repository`() =
        runTest {
            viewModel.toggleSound(false)
            advanceUntilIdle() // Ensures coroutine execution
            coVerify { mockSettingsRepository.setSound(false) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `toggleNotifications should call repository`() =
        runTest {
            viewModel.toggleNotifications(false)
            advanceUntilIdle()
            coVerify { mockSettingsRepository.setNotifications(false) }
        }

    @Test
    fun `uiState should update when repository values change`() =
        runTest {
            viewModel.uiState.test {
                assertEquals(SettingsUiState(DarkMode.SYSTEM, true, true), awaitItem())

                darkModeFlow.value = DarkMode.DARK
                assertEquals(SettingsUiState(DarkMode.DARK, true, true), awaitItem())

                soundFlow.value = false
                assertEquals(SettingsUiState(DarkMode.DARK, false, true), awaitItem())

                notificationsFlow.value = false
                assertEquals(SettingsUiState(DarkMode.DARK, false, false), awaitItem())
            }
        }
}
