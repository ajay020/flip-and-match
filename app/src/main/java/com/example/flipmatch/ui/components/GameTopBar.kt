package com.example.flipmatch.ui.components

import FlipMatchTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(modifier: Modifier = Modifier, level:Int = 0,  onClose: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.primaryContainer,
        ),

        title = {
            Text(
                text = "Level $level",
                style = MaterialTheme.typography.titleLarge)
                },
        navigationIcon = {
            IconButton(
                onClick = { onClose() },
            ) {
                Icon(
                    Icons.Default.Close, contentDescription = "Close",
                    modifier = Modifier.size(34.dp),
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun GameTopBarPreview() {
        FlipMatchTheme (
            darkTheme = false
        ){
          GameTopBar(onClose = {})
        }
}
