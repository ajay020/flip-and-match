package com.example.flipmatch.ui.leaderboard

import com.example.flipmatch.data.model.User
import com.example.flipmatch.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LeaderboardViewModelTest {
    private lateinit var viewModel: LeaderboardViewModel
    private val mockRepository: UserRepository = mockk() // Mock the repository

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher()) // Set test dispatcher
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset dispatcher
    }

    @Test
    fun `fetchLeaderboard() updates leaderboard correctly`() =
        runTest {
            // Arrange: Fake users list
            val fakeUsers = listOf(User("Alice"), User("Bob"))

            // Mock repository to return fake data as Flow
            coEvery { mockRepository.getTopUsersWithCurrent() } returns fakeUsers

            // Act: Create ViewModel (fetchLeaderboard() is called in init block)
            viewModel = LeaderboardViewModel(mockRepository)
            advanceUntilIdle() // Ensures coroutines complete execution

            // Assert: Check if leaderboard is updated correctly
            assertEquals(fakeUsers, viewModel.leaderboard.value)
        }

    @Test
    fun `fetchLeaderboard() calls repository method exactly once`() =
        runTest {
            // Arrange
            coEvery { mockRepository.getTopUsersWithCurrent() } returns emptyList()

            // Act
            viewModel = LeaderboardViewModel(mockRepository)
            advanceUntilIdle()

            // Assert: Verify getTopUsers() was called once
            coVerify(exactly = 1) { mockRepository.getTopUsersWithCurrent() }
        }
}
