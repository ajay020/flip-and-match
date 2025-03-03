package com.example.flipmatch

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flipmatch.ui.login.LoginScreen
import com.example.flipmatch.ui.main.MainScreen
import com.example.flipmatch.ui.theme.FlipMatchTheme
import com.example.flipmatch.utils.Routes
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FlipAndMatchApp() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) Routes.HOME else Routes.LOGIN

    FlipMatchTheme {
        NavHost(navController, startDestination = startDestination) {
            composable(Routes.LOGIN) {
                LoginScreen(navController)
            }
            composable(Routes.HOME) {
                MainScreen()
            }
        }
    }
}
