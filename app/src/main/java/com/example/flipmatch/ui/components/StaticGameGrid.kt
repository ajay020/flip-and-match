package com.example.flipmatch.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StaticGameGrid() {
    val emojis =
        remember {
            mutableStateListOf(
                "😀",
                "😂",
                "😎",
                "😂",
                "😎",
                "🤩",
                "🤩",
                "😇",
                "😀",
            )
        }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (row in 0 until 3) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    EmojiGridItem(emoji = emojis.getOrElse(index) { "" }, index = index)
                }
            }
        }
    }
}

@Composable
fun EmojiGridItem(
    emoji: String,
    index: Int,
) {
    var isVisible by remember { mutableStateOf(true) }

    Card(
        modifier =
            Modifier
                .size(60.dp)
                .padding(4.dp)
                .clickable {
                    isVisible = true
                },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            if ( index == 2 || index == 4) {
                Text(
                    text = emoji,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StaticGameGridPreview() {
    StaticGameGrid()
}

