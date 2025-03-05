package com.example.flipmatch.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flipmatch.data.model.PuzzleCard

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
                        Text("", fontSize = 32.sp) // Back of the card
                    }
                }
            }
        }
    }
}
