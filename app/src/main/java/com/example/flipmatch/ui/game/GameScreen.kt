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
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            GameTopBar(
                modifier = Modifier,
                level = uiState.level,
                onClose = { navController.popBackStack() },
            )
        },
    ) { paddingValues ->
        MainContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onCardClick = { cardIndex -> viewModel.flipCard(cardIndex) },
            onHintClick = { viewModel.useHint() },
            onExtraTimeClick = { viewModel.addExtraTime() },
        )

        if (uiState.isGameCompleted) {
            GameCompleteDialog(
                score = uiState.score,
                onNextGame = {
                    viewModel.startNewGame()
                },
                onQuit = {
                    viewModel.closeCompletionDialog()
                    navController.popBackStack()
                },
            )
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    uiState: GameUiState,
    onCardClick: (Int) -> Unit,
    onHintClick: () -> Unit,
    onExtraTimeClick: () -> Unit,
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
            remainingTime = uiState.remainingTime,
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
                movesCount = uiState.movesCount,
                score = uiState.score,
            )
            Spacer(Modifier.height(16.dp))
            // Grid content
            GameGrid(
                modifier = Modifier.padding(horizontal = 16.dp),
                cards = uiState.cards,
                onCardClick = { onCardClick(it) },
            )
        }

        // Hint & Extra Time Buttons (NEW)
        HintAndExtraTimeButtons(
            hintsRemaining = uiState.hintsRemaining,
            extraTimeRemaining = uiState.extraTimeRemaining,
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

    val uiState =
        GameUiState(
            cards = cards,
            isGameCompleted = false,
            movesCount = 0,
            score = 0,
            remainingTime = 30,
        )

    MainContent(
        uiState = uiState,
        onCardClick = {},
        onHintClick = {},
        onExtraTimeClick = {},
    )
}
