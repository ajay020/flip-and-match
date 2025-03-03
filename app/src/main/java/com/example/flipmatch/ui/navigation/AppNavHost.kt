package com.example.flipmatch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flipmatch.ui.game.GameScreen
import com.example.flipmatch.ui.home.HomeScreen
import com.example.flipmatch.ui.leaderboard.LeaderboardScreen
import com.example.flipmatch.ui.profile.ProfileScreen
import com.example.flipmatch.utils.Routes

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    rootNavController: NavHostController,
) {
    NavHost(navController, startDestination = Routes.HOME, modifier = modifier) {
        composable(Routes.HOME) {
            HomeScreen(
                onStartGame = { navController.navigate(Routes.GAME) },
            )
        }
        composable(Routes.GAME) {
            GameScreen(navController)
        }
        composable(Routes.LEADERBOARD) {
            LeaderboardScreen()
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onLogout = {
                    rootNavController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) {
                            inclusive = true // Clear MAIN screen from backstack
                        }
                    }
                },
            )
        }
    }
}
