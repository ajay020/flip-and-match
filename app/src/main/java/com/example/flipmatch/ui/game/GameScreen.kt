package com.example.flipmatch.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flipmatch.data.model.Puzzle
import com.example.flipmatch.data.model.PuzzleCard
import kotlin.math.sqrt

@Composable
fun GameScreen(
    modifier: Modifier,
    viewModel: GameViewModel = viewModel(),
) {
    val cards by viewModel.cards.collectAsState()

    GridContent(
        modifier = modifier,
        cards = cards,
        onCardClick = { cardIndex -> viewModel.flipCard(cardIndex) },
    )
}

@Composable
fun GridContent(
    modifier: Modifier = Modifier,
    cards: List<PuzzleCard>,
    onCardClick: (Int) -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.Green),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier =
                modifier
                    .size(width = 400.dp, height = 400.dp)
                    .background(Color.Gray)
                    .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val gridSize = sqrt(cards.size.toDouble()).toInt()
            for (row in 0 until gridSize) {
                Row(modifier = Modifier.padding(2.dp)) {
                    for (col in 0 until gridSize) {
                        val cardIndex = row * gridSize + col
                        FlipCard(
                            card = cards[cardIndex],
                            onCardClick = {
                                onCardClick(cardIndex)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FlipCard(
    card: PuzzleCard,
    onCardClick: () -> Unit = {},
) {
    Card(
        modifier =
            Modifier
                .padding(4.dp)
                .size(80.dp)
                .clickable { onCardClick() },
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
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

@Preview(showBackground = true)
@Composable
fun GridContentPreview() {
    val cards =
        listOf(
            PuzzleCard(
                id = 1,
                imageRes = null,
                emoji = "❤️",
            ),
            PuzzleCard(
                id = 2,
                imageRes = null,
                emoji = "🥰",
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
                emoji = "😱",
            ),
            PuzzleCard(
                id = 8,
                imageRes = null,
                emoji = "🤢",
            ),
        )
    GridContent(
        modifier = Modifier,
        cards = cards,
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
