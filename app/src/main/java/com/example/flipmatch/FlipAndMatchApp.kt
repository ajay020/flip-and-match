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
    val rootNavController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) Routes.MAIN else Routes.LOGIN

    FlipMatchTheme {
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
