package com.example.flipmatch.ui.game

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.flipmatch.MainDispatcherRule
import com.example.flipmatch.data.model.Puzzle
import com.example.flipmatch.data.model.PuzzleCard
import com.example.flipmatch.data.repository.PuzzleRepository
import com.example.flipmatch.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GameViewModelTest {
    /**
     *  InstantTaskExecutorRule is a JUnit rule
     *  It forces LiveData updates to execute immediately during unit tests,
     *  avoiding unexpected failures due to delayed updates.
     */

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * This rule ensures that Flows run on a test coroutine dispatcher instead of being suspended indefinitely.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: GameViewModel
    private val repository = mockk<PuzzleRepository>()
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val auth = mockk<FirebaseAuth>()
    private val fakeUser = mockk<FirebaseUser>()

    @Before
    fun setUp() {
        every { auth.currentUser } returns fakeUser
        every { fakeUser.uid } returns "test_uid"
        every { repository.getPuzzles() } returns
            listOf(
                Puzzle(
                    id = 1,
                    gridSize = 2,
                    images =
                        listOf(
                            PuzzleCard(id = 1, emoji = "✅"),
                            PuzzleCard(id = 2, emoji = "🦒"),
                        ),
                    difficulty = "easy",
                ),
            )
        viewModel = GameViewModel(repository, userRepository, auth)
    }

    @Test
    fun `flipCard updates UI state correctly`() =
        runTest {
            viewModel.uiState.test {
                val initialState = awaitItem()
                assertEquals(initialState.cards.count { it.isFlipped }, 0)

                viewModel.flipCard(0)
                val updatedState = awaitItem()
                assertEquals(updatedState.cards[0].isFlipped, true)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `matching cards increases score`() =
        runTest {
            viewModel.uiState.test {
                val initialState = awaitItem() // Ensure we have the initial state

                // Find the first matching pair
                val firstCard = initialState.cards.first()
                val firstIndex = initialState.cards.indexOfFirst { it.id == firstCard.id }
                val secondIndex = initialState.cards.indexOfLast { it.id == firstCard.id } // Get the last occurrence

                viewModel.flipCard(firstIndex)
                viewModel.flipCard(secondIndex)

                advanceUntilIdle() // Wait for state update

                val stateAfterMatch = expectMostRecentItem()
                assertEquals(10, stateAfterMatch.score)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `non-matching cards decrease score`() =
        runTest {
            viewModel.uiState.test {
                val initialState = awaitItem()

                val firstCard = initialState.cards.first()
                val secondCardIndex = initialState.cards.indexOfLast { it.id != firstCard.id }

                viewModel.flipCard(0)
                viewModel.flipCard(secondCardIndex)
                advanceUntilIdle()

                val stateAfterMismatch = expectMostRecentItem()
                assertEquals(-2, stateAfterMismatch.score)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `game completes when all cards are matched`() =
        runTest {
            viewModel.uiState.test {
                val initialState = awaitItem()

                // Flip all matching pairs dynamically
                val flippedIndices = mutableSetOf<Int>()
                for (card in initialState.cards) {
                    if (card.id !in flippedIndices) {
                        val firstIndex = initialState.cards.indexOf(card)
                        val secondIndex = initialState.cards.indexOfLast { it.id == card.id }

                        viewModel.flipCard(firstIndex)
                        viewModel.flipCard(secondIndex)
                        flippedIndices.add(card.id)
                        advanceUntilIdle()
                    }
                }

                val finalState = expectMostRecentItem()
                assertEquals(true, finalState.isGameCompleted)
            }
        }

    @Test
    fun `startNewGame resets game state`() =
        runTest {
            viewModel.startNewGame()

            val newState = viewModel.uiState.first()
            assertEquals(newState.isGameCompleted, false)
            assertEquals(newState.score, 0)
            assertEquals(newState.movesCount, 0)
        }
}
