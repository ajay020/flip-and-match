package com.example.flipmatch.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MovesAndScore(
    modifier: Modifier = Modifier,
    movesCount: Int,
    score: Int,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("Moves: $movesCount", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Score: $score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
private fun MovesAndScore() {
    MovesAndScore(movesCount = 5, score = 5)
}
