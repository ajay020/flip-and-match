package com.example.flipmatch.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flipmatch.data.model.PuzzleCard
import kotlin.math.sqrt

@Composable
fun GameGrid(
    modifier: Modifier = Modifier,
    cards: List<PuzzleCard>,
    onCardClick: (Int) -> Unit,
) {
    val gridSize = sqrt(cards.size.toDouble()).toInt()

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier =
        modifier
            .padding(4.dp),
    ) {
        itemsIndexed(cards) { index, card ->
            FlipCard(
                modifier =
                Modifier
                    .padding(4.dp),
                card = card,
                onCardClick = {
                    if (cards[index].id != -1) {
                        onCardClick(index)
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GridContentPreview() {
    val cards =
        listOf(
            PuzzleCard(
                id = 1,
                imageRes = null,
                emoji = null,
            ),
            PuzzleCard(
                id = 2,
                imageRes = null,
                emoji = "🥰",
                isFlipped = true,
            ),
            PuzzleCard(
                id = 3,
                imageRes = null,
                emoji = "😂",
            ),
            PuzzleCard(
                id = 4,
                imageRes = null,
                emoji = "😎",
            ),
            PuzzleCard(
                id = 5,
                imageRes = null,
                emoji = "⌛",
            ),
            PuzzleCard(
                id = 6,
                imageRes = null,
                emoji = "✅",
            ),
            PuzzleCard(
                id = 7,
                imageRes = null,
                emoji = "✅",
            ),
            PuzzleCard(
                id = 8,
                imageRes = null,
                emoji = "✅",
            ),
        )

    val gridSize = 4
    val imageCards = (cards + cards).take(gridSize * gridSize)
    val emptySlots = (gridSize * gridSize) - imageCards.size
    val cardsWithEmptySlots = (imageCards + List(emptySlots) { PuzzleCard.EMPTY }).shuffled()

    GameGrid(
        modifier = Modifier,
        cards = cardsWithEmptySlots,
        onCardClick = {},
    )
}