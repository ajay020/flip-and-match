package com.example.flipmatch

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.flipmatch.ui.navigation.AppNavHost
import com.example.flipmatch.ui.theme.FlipMatchTheme

@Composable
fun FlipAndMatchApp() {
    FlipMatchTheme {
        AppNavHost()
    }
}
