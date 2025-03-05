package com.example.flipmatch.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flipmatch.data.model.PuzzleCard
import com.example.flipmatch.ui.components.FlipCard
import com.example.flipmatch.ui.components.GameCompleteDialog
import com.example.flipmatch.ui.components.GameTopBar
import com.example.flipmatch.ui.components.TimerProgressBar
import kotlin.math.sqrt

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val cards by viewModel.cards.collectAsState()
    val isGameCompleted by viewModel.isGameCompleted.collectAsState()
    val remainingTime by viewModel.remainingTime.collectAsState()
    val movesCount by viewModel.movesCount.collectAsState()
    val score by viewModel.score.collectAsState()

    Scaffold(
        topBar = {
            GameTopBar(
                modifier = Modifier,
                onClose = { navController.popBackStack() },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Timer progress bar
            TimerProgressBar(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
                remainingTime = remainingTime,
                totalTime = 30,
            )

            // Display Moves & Score
            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Moves: $movesCount", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Score: $score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(16.dp))
                // Grid content
                GridContent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    cards = cards,
                    onCardClick = { cardIndex -> viewModel.flipCard(cardIndex) },
                )
            }
        }
        if (isGameCompleted) {
            GameCompleteDialog(
                score = score,
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
