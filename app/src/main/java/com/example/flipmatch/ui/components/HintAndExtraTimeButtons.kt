package com.example.flipmatch.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HintAndExtraTimeButtons(
    hintsRemaining: Int,
    extraTimeRemaining: Int,
    onHintClick: () -> Unit,
    onExtraTimeClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = onHintClick,
            modifier = Modifier.weight(1f).padding(8.dp),
            enabled = hintsRemaining > 0, // Disable when hints are zero
        ) {
            Text("Hint: ($hintsRemaining)")
        }

        Button(
            onClick = onExtraTimeClick,
            modifier = Modifier.weight(1f).padding(8.dp),
            enabled = extraTimeRemaining > 0, // Disable when extra time is used up
        ) {
            Text("Extra Time ($extraTimeRemaining)")
        }
    }
}
