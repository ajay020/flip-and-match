package com.example.flipmatch.ui.profile

import app.cash.turbine.test
import com.example.flipmatch.MainDispatcherRule
import com.example.flipmatch.data.model.User
import com.example.flipmatch.data.repository.UserRepository
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {
    // Use TestCoroutineRule to run ViewModelScope coroutines in a controlled environment
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ProfileViewModel
    private val userRepository: UserRepository = mockk()

    @Before
    fun setup() {
        // Mock the userRepository response
        coEvery { userRepository.getUserData() } returns User(uid = "1", name = "Joy")

        viewModel = ProfileViewModel(userRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchUserData() updates userData state when repository emits user`() =
        runTest {
            // Use Turbine to collect StateFlow values
            viewModel.userData.test {
                advanceUntilIdle() // Wait for coroutine execution
                val user = awaitItem()
                Assert.assertNotNull(user)
                Assert.assertEquals("1", user?.uid)
                Assert.assertEquals("Joy", user?.name)
            }
        }

    @Test
    fun `logout() calls repository logout and triggers callback`() {
        val mockCallback = mockk<() -> Unit>(relaxed = true) // Mock logout callback
        every { userRepository.logout() } just Runs // Mock repository logout

        viewModel.logout(mockCallback)

        verify { userRepository.logout() } // Verify logout was called
        verify { mockCallback() } // Verify callback was triggered
    }
}
