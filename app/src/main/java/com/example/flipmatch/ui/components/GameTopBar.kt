package com.example.flipmatch.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(onClose: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text("Game") },
        navigationIcon = {
            IconButton(
                onClick = { onClose() },
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        },
    )
}