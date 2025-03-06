package com.example.flipmatch.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
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
        ActionButtonWithText(
            icon = Icons.Default.Lightbulb,
            text = "Hint ($hintsRemaining)",
            onClick = onHintClick,
            enabled = hintsRemaining > 0,
        )

        ActionButtonWithText(
            icon = Icons.Default.Timer,
            text = "Extra Time ($extraTimeRemaining)",
            onClick = onExtraTimeClick,
            enabled = extraTimeRemaining > 0,
        )
    }
}

@Composable
fun ActionButtonWithText(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier =
                Modifier
                    .size(64.dp)
                    .border(
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                    ).padding(4.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(48.dp),
            )
        }
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun HintAndExtraTimeButtonsPreview() {
    HintAndExtraTimeButtons(
        hintsRemaining = 2,
        extraTimeRemaining = 3,
        onHintClick = { },
        onExtraTimeClick = { },
    )
}
