package com.example.flipmatch.data.repository

import com.example.flipmatch.MainDispatcherRule
import com.example.flipmatch.data.DataStoreManager
import com.example.flipmatch.utils.DarkMode
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dataStoreManager = mockk<DataStoreManager>(relaxed = true)
    private lateinit var settingsRepository: SettingsRepository

    @Before
    fun setup() {
        settingsRepository = SettingsRepository(dataStoreManager)
    }

    @Test
    fun `darkMode flow should return data from DataStoreManager`() =
        runTest {
            val expectedDarkMode = DarkMode.DARK
            every { dataStoreManager.darkMode } returns flowOf(expectedDarkMode)

            settingsRepository.darkMode.collect {
                assertEquals(expectedDarkMode, it)
            }
        }

    @Test
    fun `sound flow should return data from DataStoreManager`() =
        runTest {
            val expectedSound = true
            every { dataStoreManager.sound } returns flowOf(expectedSound)

            settingsRepository.sound.collect {
                assertEquals(expectedSound, it)
            }
        }

    @Test
    fun `notifications flow should return data from DataStoreManager`() =
        runTest {
            val expectedNotifications = false
            every { dataStoreManager.notifications } returns flowOf(expectedNotifications)

            settingsRepository.notifications.collect {
                assertEquals(expectedNotifications, it)
            }
        }

    @Test
    fun `setDarkMode should call DataStoreManager setDarkMode`() =
        runTest {
            val darkModeToSet = DarkMode.LIGHT
            settingsRepository.setDarkMode(darkModeToSet)
            coVerify { dataStoreManager.setDarkMode(darkModeToSet) }
        }

    @Test
    fun `setSound should call DataStoreManager setSound`() =
        runTest {
            val soundToSet = true
            settingsRepository.setSound(soundToSet)
            coVerify { dataStoreManager.setSound(soundToSet) }
        }

    @Test
    fun `setNotifications should call DataStoreManager setNotifications`() =
        runTest {
            val notificationsToSet = false
            settingsRepository.setNotifications(notificationsToSet)
            coVerify { dataStoreManager.setNotifications(notificationsToSet) }
        }
}
