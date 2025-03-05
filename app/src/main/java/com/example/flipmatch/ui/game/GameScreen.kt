package com.example.flipmatch.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flipmatch.data.model.PuzzleCard
import com.example.flipmatch.ui.components.GameCompleteDialog
import com.example.flipmatch.ui.components.GameGrid
import com.example.flipmatch.ui.components.GameTopBar
import com.example.flipmatch.ui.components.HintAndExtraTimeButtons
import com.example.flipmatch.ui.components.MovesAndScore
import com.example.flipmatch.ui.components.TimerProgressBar

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
    val hintsRemaining by viewModel.hintsRemaining.collectAsState()
    val extraTimeRemaining by viewModel.extraTimeRemaining.collectAsState()

    Scaffold(
        topBar = {
            GameTopBar(
                modifier = Modifier,
                onClose = { navController.popBackStack() },
            )
        },
    ) { paddingValues ->
        MainContent(
            modifier = Modifier.padding(paddingValues),
            remainingTime = remainingTime,
            cards = cards,
            movesCount = movesCount,
            score = score,
            hintsRemaining = hintsRemaining,
            extraTimeRemaining = extraTimeRemaining,
            onCardClick = { cardIndex -> viewModel.flipCard(cardIndex) },
            onHintClick = { viewModel.useHint() },
            onExtraTimeClick = { viewModel.addExtraTime() },
        )

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
fun MainContent(
    modifier: Modifier = Modifier,
    cards: List<PuzzleCard> = emptyList(),
    remainingTime: Int = 0,
    movesCount: Int = 0,
    score: Int = 0,
    hintsRemaining: Int = 0,
    extraTimeRemaining: Int = 0,
    onCardClick: (Int) -> Unit,
    onHintClick: () -> Unit = {},
    onExtraTimeClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // Timer progress bar
        TimerProgressBar(
            modifier =
                Modifier
                    .padding(16.dp),
            remainingTime = remainingTime,
            totalTime = 30,
        )

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            // Display Moves & Score
            MovesAndScore(
                modifier = Modifier.padding(horizontal = 16.dp),
                movesCount = movesCount,
                score = score,
            )
            Spacer(Modifier.height(16.dp))
            // Grid content
            GameGrid(
                modifier = Modifier.padding(horizontal = 16.dp),
                cards = cards,
                onCardClick = { onCardClick(it) },
            )
        }

        // Hint & Extra Time Buttons (NEW)
        HintAndExtraTimeButtons(
            hintsRemaining = hintsRemaining,
            extraTimeRemaining = extraTimeRemaining,
            onHintClick = onHintClick,
            onExtraTimeClick = onExtraTimeClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainContentPreview() {
    val cards =
        listOf(
            PuzzleCard(
                id = 1,
                imageRes = null,
                emoji = "😂",
            ),
            PuzzleCard(
                id = 2,
                imageRes = null,
                emoji = "😂",
            ),
            PuzzleCard(
                id = 3,
                imageRes = null,
                emoji = "😂",
            ),
            PuzzleCard(
                id = 4,
                imageRes = null,
                emoji = "😂",
            ),
        )
    MainContent(
        cards = cards,
        remainingTime = 10,
        movesCount = 10,
        score = 10,
        extraTimeRemaining = 0,
        hintsRemaining = 0,
        onCardClick = {},
        onHintClick = {},
        onExtraTimeClick = {},
    )
}
