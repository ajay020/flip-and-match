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
import kotlinx.coroutines.flow.collectLatest
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
        private val _cards = MutableStateFlow<List<PuzzleCard>>(emptyList())
        val cards: StateFlow<List<PuzzleCard>> = _cards

        private val _isGameCompleted = MutableStateFlow(false)
        val isGameCompleted: StateFlow<Boolean> = _isGameCompleted

        private val _movesCount = MutableStateFlow(0) // Track moves
        val movesCount: StateFlow<Int> = _movesCount

        private val _score = MutableStateFlow(0) // Track score
        val score: StateFlow<Int> = _score

        private val _remainingTime = MutableStateFlow(30)
        val remainingTime: StateFlow<Int> = _remainingTime

        private val _hintsRemaining = MutableStateFlow(2) // User gets 2 hints
        val hintsRemaining: StateFlow<Int> = _hintsRemaining

        private var firstSelectedCard: PuzzleCard? = null
        private var secondSelectedCard: PuzzleCard? = null
        private var currentPuzzle: Puzzle? = null
        private var isFlipAllowed = true
        private var currentPuzzleIndex = 0
        private var countdownJob: Job? = null
        private var hintsUsed = 0
        private val maxHints = 2

        init {
            loadPuzzles()
            startCountdown()
            observeCards()
        }

        private fun observeCards() {
            // Observe cards to check for completion
            viewModelScope.launch {
                _cards.collectLatest { cards ->
                    if (cards.isNotEmpty() && cards.all { it.isMatched || it.id == -1 }) {
                        calculateFinalScore()
                        currentPuzzle?.let {
                            updateScore(it.difficulty, _score.value)
                        }
                        completeGame()
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
                userRepository
                    .updateUserScore(uid, puzzleType, newScore)
                    .collect { success ->
                        if (success) {
                            Log.d("ProfileViewModel", "Score updated successfully!")
                        } else {
                            Log.e("ProfileViewModel", "Failed to update score.")
                        }
                    }
            }
        }

        fun useHint() {
            if (_hintsRemaining.value <= 0) return // Disable if no hints left

            val unmatchedPairs =
                _cards.value
                    .filter { !it.isMatched && !it.isFlipped }
                    .groupBy { it.id }
                    .values
                    .filter { it.size == 2 } // Ensure we get pairs

            if (unmatchedPairs.isNotEmpty()) {
                val pairToReveal = unmatchedPairs.random() // Pick a random unmatched pair

                _cards.value =
                    _cards.value.map { card ->
                        if (pairToReveal.any { it.id == card.id }) {
                            card.copy(isFlipped = true, isMatched = true)
                        } else {
                            card
                        }
                    }

                _hintsRemaining.value -= 1 // Reduce hint count
            }
        }

        private fun startCountdown() {
            countdownJob?.cancel() // Cancel any existing timer
            countdownJob =
                viewModelScope.launch {
                    while (_remainingTime.value > 0) {
                        delay(1000L) // Wait for 1 second
                        _remainingTime.value -= 1
                    }
                    completeGame() // Auto-complete game if time runs out
                }
        }

        private fun loadPuzzles() {
            val puzzles = repository.getPuzzles()
            val puzzle = puzzles[currentPuzzleIndex]
            currentPuzzle = puzzle
            val gridSize = puzzle.gridSize // Use fixed grid size
            val totalCells = gridSize * gridSize

            // Duplicate images to form pairs
            val imagePairs = puzzle.images.flatMap { listOf(it, it) }

            // Ensure there are enough images to fill the grid
            val imageCards = imagePairs.shuffled().take(totalCells)

            // If we don't have enough image cards, fill remaining slots with empty cards
            val emptySlots = totalCells - imageCards.size
            val finalCards = imageCards + List(emptySlots) { PuzzleCard.EMPTY }

            _cards.value = finalCards.shuffled()
        }

        fun flipCard(cardIndex: Int) {
            // Don't allow flipping if it's not the user's turn
            if (!isFlipAllowed) return
            val card = _cards.value[cardIndex]

            // the card is already flipped or matched, don't flip again
            if (card.isFlipped || card.isMatched) return

            updateCard(cardIndex, card.copy(isFlipped = true))

            when {
                firstSelectedCard == null -> firstSelectedCard = card
                secondSelectedCard == null -> {
                    secondSelectedCard = card
                    _movesCount.value += 1 // Increase move count
                    checkForMatch()
                }
            }
        }

        private fun checkForMatch() {
            if (firstSelectedCard == null || secondSelectedCard == null) return
            isFlipAllowed = false // Disable further clicks

            if (firstSelectedCard!!.id == secondSelectedCard!!.id) {
                matchCards(firstSelectedCard!!)
                _score.value += 10 // Add 10 points for a match
                resetSelection()
            } else {
                _score.value -= 2 // Deduct 2 points for an incorrect match
                viewModelScope.launch {
                    delay(500)
                    flipBackCards()
                }
            }
        }

        private fun updateCard(
            index: Int,
            card: PuzzleCard,
        ) {
            _cards.value =
                _cards.value.mapIndexed { i, puzzleCard ->
                    if (i == index) card else puzzleCard
                }
        }

        private fun matchCards(card: PuzzleCard) {
            _cards.value =
                _cards.value.map { puzzleCard ->
                    if (card.id == puzzleCard.id) puzzleCard.copy(isMatched = true) else puzzleCard
                }
            resetSelection()
        }

        private fun flipBackCards() {
            _cards.value =
                _cards.value.map {
                    if (it.id == firstSelectedCard!!.id || it.id == secondSelectedCard!!.id) {
                        it.copy(isFlipped = false)
                    } else {
                        it
                    }
                }
            resetSelection()
        }

        private fun resetSelection() {
            firstSelectedCard = null
            secondSelectedCard = null
            isFlipAllowed = true
        }

        private fun completeGame() {
            _isGameCompleted.value = true
            countdownJob?.cancel()
        }

        fun startNewGame() {
            _isGameCompleted.value = false
            _movesCount.value = 0
            _score.value = 0
            _remainingTime.value = 30
            currentPuzzleIndex++
            if (currentPuzzleIndex < repository.getPuzzles().size) {
                loadPuzzles()
                startCountdown()
            }
        }

        private fun calculateFinalScore() {
            val timeBonus = _remainingTime.value / 2 // Give extra points for remaining time
            _score.value += timeBonus
        }
    }
