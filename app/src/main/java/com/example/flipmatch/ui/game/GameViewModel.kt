package com.example.flipmatch.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipmatch.data.model.Puzzle
import com.example.flipmatch.data.model.PuzzleCard
import com.example.flipmatch.data.repository.PuzzleRepository
import com.example.flipmatch.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel
    @Inject
    constructor(
        private val repository: PuzzleRepository,
        private val userRepository: UserRepository,
        private val auth: FirebaseAuth,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(GameUiState())
        val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

        private var firstSelectedCard: PuzzleCard? = null
        private var secondSelectedCard: PuzzleCard? = null
        private var currentPuzzle: Puzzle? = null
        private var isFlipAllowed = true
        private var currentPuzzleIndex = 0
        private var countdownJob: Job? = null

        init {
            loadPuzzles()
            startCountdown()
            observeCards()
        }

        private fun observeCards() {
            viewModelScope.launch {
                _uiState
                    .map { it.cards } // Extract only the cards list
                    .distinctUntilChanged() // Prevent unnecessary emissions
                    .collect { cards ->
                        if (cards.isNotEmpty() && cards.all { it.isMatched || it.id == -1 }) {
                            if (!_uiState.value.isGameCompleted) { // Prevent multiple triggers
                                calculateFinalScore()
                                currentPuzzle?.let {
                                    updateScore(it.difficulty, _uiState.value.score)
                                }
                                completeGame()
                            }
                        }
                    }
            }
        }

        private fun updateScore(
            puzzleType: String,
            newScore: Int,
        ) {
            val uid = auth.currentUser?.uid ?: return
            viewModelScope.launch {
                userRepository.updateUserScore(uid, puzzleType, newScore).collect { success ->
                    if (success) {
                        Log.d("GameViewModel", "Score updated successfully!")
                    } else {
                        Log.e("GameViewModel", "Failed to update score.")
                    }
                }
            }
        }

        private fun startCountdown() {
            countdownJob?.cancel()
            countdownJob =
                viewModelScope.launch {
                    while (_uiState.value.remainingTime > 0) {
                        delay(1000L)
                        _uiState.update { it.copy(remainingTime = it.remainingTime - 1) }
                    }
                    completeGame()
                }
        }

        private fun loadPuzzles() {
            val puzzles = repository.getPuzzles()
            if (currentPuzzleIndex >= puzzles.size) return

            val puzzle = puzzles[currentPuzzleIndex]
            currentPuzzle = puzzle
            val gridSize = puzzle.gridSize
            val totalCells = gridSize * gridSize
            val imagePairs = puzzle.images.flatMap { listOf(it, it) }
            val imageCards = imagePairs.shuffled().take(totalCells)
            val emptySlots = totalCells - imageCards.size
            val finalCards = imageCards + List(emptySlots) { PuzzleCard.EMPTY }

            _uiState.update { it.copy(cards = finalCards.shuffled()) }
        }

        fun flipCard(cardIndex: Int) {
            val state = _uiState.value
            if (!isFlipAllowed) return
            val card = state.cards[cardIndex]

            if (card.isFlipped || card.isMatched) return

            val updatedCards = state.cards.toMutableList()
            updatedCards[cardIndex] = card.copy(isFlipped = true)
            _uiState.update { it.copy(cards = updatedCards) }

            when {
                firstSelectedCard == null -> firstSelectedCard = card
                secondSelectedCard == null -> {
                    secondSelectedCard = card
                    _uiState.update { it.copy(movesCount = it.movesCount + 1) }
                    checkForMatch()
                }
            }
        }

        private fun checkForMatch() {
            if (firstSelectedCard == null || secondSelectedCard == null) return
            isFlipAllowed = false

            if (firstSelectedCard!!.id == secondSelectedCard!!.id) {
                matchCards(firstSelectedCard!!)
                _uiState.update { it.copy(score = it.score + 10) }
            } else {
                _uiState.update { it.copy(score = it.score - 2) }
                viewModelScope.launch {
                    delay(500)
                    flipBackCards()
                }
            }
        }

        private fun matchCards(card: PuzzleCard) {
            _uiState.update { state ->
                state.copy(
                    cards =
                        state.cards.map {
                            if (it.id == card.id) it.copy(isMatched = true) else it
                        },
                )
            }
            resetSelection()
        }

        private fun flipBackCards() {
            _uiState.update { state ->
                state.copy(
                    cards =
                        state.cards.map {
                            if (it.id == firstSelectedCard!!.id || it.id == secondSelectedCard!!.id) {
                                it.copy(isFlipped = false)
                            } else {
                                it
                            }
                        },
                )
            }
            resetSelection()
        }

        fun closeCompletionDialog() {
            _uiState.update { it.copy(isGameCompleted = false) }
        }

        private fun resetSelection() {
            firstSelectedCard = null
            secondSelectedCard = null
            isFlipAllowed = true
        }

        private fun completeGame() {
            _uiState.update { it.copy(isGameCompleted = true) }
            countdownJob?.cancel()
        }

        fun startNewGame() {
            _uiState.update {
                it.copy(
                    isGameCompleted = false,
                    movesCount = 0,
                    score = 0,
                    remainingTime = 30,
                    hintsRemaining = 2,
                    level = it.level + 1,
                )
            }
            currentPuzzleIndex++
            if (currentPuzzleIndex < repository.getPuzzles().size) {
                loadPuzzles()
                startCountdown()
            }
        }

        private fun calculateFinalScore() {
            val timeBonus = _uiState.value.remainingTime / 2
            _uiState.update { it.copy(score = it.score + timeBonus) }
        }

        fun useHint() {
            val state = _uiState.value
            if (state.hintsRemaining <= 0) return

            val unmatchedPairs =
                state.cards
                    .filter { !it.isMatched && !it.isFlipped }
                    .groupBy { it.id }
                    .values
                    .filter { it.size == 2 }

            if (unmatchedPairs.isNotEmpty()) {
                val pairToReveal = unmatchedPairs.random()

                _uiState.update {
                    it.copy(
                        cards =
                            it.cards.map { card ->
                                if (pairToReveal.any { it.id == card.id }) {
                                    card.copy(isFlipped = true, isMatched = true)
                                } else {
                                    card
                                }
                            },
                        hintsRemaining = it.hintsRemaining - 1,
                    )
                }
            }
        }

        fun addExtraTime() {
            _uiState.update {
                it.copy(
                    remainingTime = it.remainingTime + 15,
                    extraTimeRemaining = it.extraTimeRemaining - 1,
                )
            }
        }
    }

data class GameUiState(
    val cards: List<PuzzleCard> = emptyList(),
    val isGameCompleted: Boolean = false,
    val movesCount: Int = 0,
    val score: Int = 0,
    val remainingTime: Int = 30,
    val hintsRemaining: Int = 2,
    val extraTimeRemaining: Int = 2,
    val level: Int = 1,
)
