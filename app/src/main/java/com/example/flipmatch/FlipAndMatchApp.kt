package com.example.flipmatch

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.flipmatch.ui.game.GameScreen
import com.example.flipmatch.ui.theme.FlipMatchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlipAndMatchApp() {
    FlipMatchTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Flip and Match") },
                )
            },
        ) { innerPadding ->
            GameScreen(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
