package com.example.flipmatch.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameCompleteDialog(
    onNextGame: () -> Unit,
    onQuit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { /* Prevent dismiss on outside click */ },
        title = { Text("Game Completed!") },
        text = { Text("Do you want to play the next game or quit?") },
        confirmButton = {
            Button(onClick = onNextGame) {
                Text("Next")
            }
        },
        dismissButton = {
            Button(onClick = onQuit) {
                Text("Quit")
            }
        },
    )
}
