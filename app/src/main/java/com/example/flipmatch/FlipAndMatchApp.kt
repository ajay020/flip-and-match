package com.example.flipmatch

import FlipMatchTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flipmatch.ui.login.LoginScreen
import com.example.flipmatch.ui.main.MainScreen
import com.example.flipmatch.ui.settings.SettingsViewModel
import com.example.flipmatch.utils.DarkMode
import com.example.flipmatch.utils.Routes
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FlipAndMatchApp(viewModel: SettingsViewModel = hiltViewModel()) {
    val rootNavController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) Routes.MAIN else Routes.LOGIN
    val darkMode by viewModel.darkMode.collectAsState()

    val isDarkTheme =
        when (darkMode) {
            DarkMode.DARK -> true
            DarkMode.LIGHT -> false
            DarkMode.SYSTEM -> isSystemInDarkTheme()
        }

    FlipMatchTheme(
        darkTheme = isDarkTheme,
    ) {
        NavHost(rootNavController, startDestination = startDestination) {
            composable(Routes.LOGIN) {
                LoginScreen(rootNavController)
            }
            composable(Routes.MAIN) {
                MainScreen(rootNavController = rootNavController)
            }
        }
    }
}
