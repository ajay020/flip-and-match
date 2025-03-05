package com.example.flipmatch.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.flipmatch.R

@Composable
fun FlipAndMatchTitle(modifier: Modifier = Modifier) {
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            1f,
            animationSpec = tween(500, easing = EaseOutBounce),
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Flip & Match",
            style =
            TextStyle(
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily(Font(R.font.chewy_regular)),
                color = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.scale(scale.value),
        )
        Text(
            "Sharpen your memory, one flip at a time!",
            style =
            TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(R.font.chewy_regular)),
                color = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.scale(scale.value),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlipAndMatchPreview() {
    FlipAndMatchTitle()
}
