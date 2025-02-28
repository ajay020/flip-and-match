package com.example.flipmatch.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipmatch.data.model.PuzzleCard
import com.example.flipmatch.data.repository.PuzzleRepository
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
    ) : ViewModel() {
        private val _cards = MutableStateFlow<List<PuzzleCard>>(emptyList())
        val cards: StateFlow<List<PuzzleCard>> = _cards

        private val _isGameCompleted = MutableStateFlow(false)
        val isGameCompleted: StateFlow<Boolean> = _isGameCompleted

        private val _remainingTime = MutableStateFlow(30)
        val remainingTime: StateFlow<Int> = _remainingTime

        private var firstSelectedCard: PuzzleCard? = null
        private var secondSelectedCard: PuzzleCard? = null
        private var isFlipAllowed = true
        private var currentPuzzleIndex = 0
        private var countdownJob: Job? = null

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
                        completeGame()
                    }
                }
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
                    checkForMatch()
                }
            }
        }

        private fun checkForMatch() {
            if (firstSelectedCard == null || secondSelectedCard == null) return
            isFlipAllowed = false // Disable further clicks

            if (firstSelectedCard!!.id == secondSelectedCard!!.id) {
                matchCards(firstSelectedCard!!)
                resetSelection()
            } else {
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
            currentPuzzleIndex++
            if (currentPuzzleIndex < repository.getPuzzles().size) {
                _remainingTime.value = 30
                loadPuzzles()
                startCountdown()
            }
        }
    }
