package com.example.flipmatch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flipmatch.ui.game.GameScreen
import com.example.flipmatch.ui.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onStartGame = { navController.navigate("game") })
        }
        composable("game") {
            GameScreen(navController = navController)
        }
    }
}
