package com.example.flipmatch.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flipmatch.data.model.PuzzleCard
import kotlin.math.sqrt

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val cards by viewModel.cards.collectAsState()
    val isGameCompleted by viewModel.isGameCompleted.collectAsState()

    Scaffold {
        GridContent(
            modifier = Modifier.padding(it),
            cards = cards,
            onCardClick = { cardIndex -> viewModel.flipCard(cardIndex) },
        )
        if (isGameCompleted) {
            GameCompleteDialog(
                onNextGame = {
                    viewModel.startNewGame()
                },
                onQuit = {
                    navController.popBackStack()
                },
            )
        }
    }
}

@Composable
fun GridContent(
    modifier: Modifier = Modifier,
    cards: List<PuzzleCard>,
    onCardClick: (Int) -> Unit,
) {
    val gridSize = sqrt(cards.size.toDouble()).toInt()

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.Gray)
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

@Composable
fun FlipCard(
    modifier: Modifier = Modifier,
    card: PuzzleCard,
    onCardClick: () -> Unit = {},
) {
    if (card.isEmpty) {
        Box(
            modifier =
                Modifier
                    .padding(4.dp)
                    .size(80.dp),
        ) {
            // Empty box (nothing rendered)
        }
    } else {
        Card(
            modifier =
                modifier
                    .padding(4.dp)
                    .size(80.dp)
                    .clickable { onCardClick() },
            shape = RoundedCornerShape(8.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                if (card.isFlipped) {
                    if (card.imageRes != null) {
                        Image(
                            painter = painterResource(id = card.imageRes),
                            contentDescription = "Card Image",
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else if (card.emoji != null) {
                        Text(
                            text = card.emoji,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("❔", fontSize = 32.sp) // Back of the card
                    }
                }
            }
        }
    }
}

@Composable
fun GameCompleteDialog(
    onNextGame: () -> Unit,
    onQuit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { /* Prevent dismiss on outside click */ },
        title = { Text("Game Completed!") },
        text = { Text("Do you want to play the next game or quit?") },
        confirmButton = {
            Button(onClick = onNextGame) {
                Text("Next")
            }
        },
        dismissButton = {
            Button(onClick = onQuit) {
                Text("Quit")
            }
        },
    )
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
        )

    val gridSize = 4
    val imageCards = (cards + cards).take(gridSize * gridSize)
    val emptySlots = (gridSize * gridSize) - imageCards.size
    val cardsWithEmptySlots = (imageCards + List(emptySlots) { PuzzleCard.EMPTY }).shuffled()

    GridContent(
        modifier = Modifier,
        cards = cardsWithEmptySlots,
        onCardClick = {},
    )
}

// @Preview(showBackground = true, widthDp = 400, heightDp = 600)
// @Composable
// private fun PuzzleCardPreview() {
//    FlipCard(
//        card =
//            PuzzleCard(
//                id = 1,
//                imageRes = null,
//                emoji = "❤️",
//                isFlipped = true,
//            ),
//    )
// }
