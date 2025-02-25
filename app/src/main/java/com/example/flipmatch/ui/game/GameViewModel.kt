package com.example.flipmatch.ui.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipmatch.data.model.PuzzleCard
import com.example.flipmatch.data.repository.PuzzleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val repository = PuzzleRepository(application)
    private val _cards = MutableStateFlow<List<PuzzleCard>>(emptyList())
    val cards: StateFlow<List<PuzzleCard>> = _cards
    private var firstSelectedCard: PuzzleCard? = null
    private var isFlipAllowed = MutableStateFlow(true)

    init {
        loadPuzzles()
    }

    private fun loadPuzzles() {
        val puzzles = repository.getPuzzles()
        val puzzle = puzzles.first()
        _cards.value = (puzzle.images + puzzle.images).shuffled()
    }

    fun flipCard(cardIndex: Int) {
        // Don't allow flipping if it's not the user's turn
        if (!isFlipAllowed.value) return

        val card = _cards.value[cardIndex]
        if (!card.isMatched && !card.isFlipped) {
            if (firstSelectedCard == null) {
                firstSelectedCard = card
                updateCard(cardIndex, card.copy(isFlipped = true))
            } else {
                updateCard(cardIndex, card.copy(isFlipped = true))
                if (card.id == firstSelectedCard!!.id) {
                    matchCards(card)
                    firstSelectedCard = null
                    isFlipAllowed.value = false
                    viewModelScope.launch {
                        delay(1000)
                        isFlipAllowed.value = true
                    }
                } else {
                    isFlipAllowed.value = false // Disable flipping
                    flipBackCards(card)
                }
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
    }

    private fun flipBackCards(card: PuzzleCard) {
        viewModelScope.launch {
            delay(1000)
            _cards.value =
                _cards.value.map { puzzleCard ->
                    if (puzzleCard.id == card.id || puzzleCard.id == firstSelectedCard!!.id) {
                        puzzleCard.copy(isFlipped = false)
                    } else {
                        puzzleCard
                    }
                }
            isFlipAllowed.value = true // Re-enable flipping
            firstSelectedCard = null
        }
    }
}
