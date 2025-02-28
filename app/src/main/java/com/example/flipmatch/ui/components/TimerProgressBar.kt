package com.example.flipmatch.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerProgressBar(
    remainingTime: Int, // Time left in seconds
    totalTime: Int = 120,
    modifier: Modifier,
) {
    val progress = remember { Animatable(1f) } // Start at full progress

    // Animate progress when remainingTime updates
    LaunchedEffect(remainingTime) {
        progress.animateTo(
            targetValue = remainingTime / totalTime.toFloat(),
            animationSpec = tween(durationMillis = 500),
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LinearProgressIndicator(
            progress = { progress.value },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
            color = Color.Green,
            trackColor = Color.Gray.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Time Left: $remainingTime sec",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 616)
@Composable
private fun TimerProgressBarPreview() {
    TimerProgressBar(
        remainingTime = 15,
        totalTime = 30,
        modifier = Modifier,
    )
}
