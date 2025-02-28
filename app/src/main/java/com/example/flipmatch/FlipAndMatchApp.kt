package com.example.flipmatch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flipmatch.ui.game.GameScreen
import com.example.flipmatch.ui.home.HomeScreen
import com.example.flipmatch.ui.leaderboard.LeaderboardScreen
import com.example.flipmatch.ui.profile.ProfileScreen
import com.example.flipmatch.ui.theme.FlipMatchTheme

@Composable
fun FlipAndMatchApp() {
    val navController = rememberNavController()

    FlipMatchTheme {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Scaffold(
            topBar = {
                if (currentRoute == "home") {
                    TopBar()
                }
            },
            bottomBar = {
                // Conditionally show the bottom bar
                AnimatedVisibility(visible = currentRoute != "game") {
                    BottomNavBar(navController)
                }
            },
        ) { innerPadding ->
            NavHost(navController, startDestination = Routes.HOME, Modifier.padding(innerPadding)) {
                composable(Routes.HOME) {
                    HomeScreen(onStartGame = { navController.navigate(Routes.GAME) })
                }
                composable(Routes.GAME) {
                    GameScreen(navController)
                }
                composable(Routes.LEADERBOARD) {
                    LeaderboardScreen()
                }
                composable(Routes.PROFILE) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items =
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Leaderboard,
            BottomNavItem.Profile,
        )

    NavigationBar {
        val currentRoute by rememberUpdatedState(
            newValue =
                navController
                    .currentBackStackEntryAsState()
                    .value
                    ?.destination
                    ?.route,
        )

        items.forEach { item ->
            NavigationBarItem(
                // Use NavigationBarItem here
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(
        title = { Text("Flip & Match") },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    )
}

// Define Routes as Constants
object Routes {
    const val HOME = "home"
    const val GAME = "game"
    const val LEADERBOARD = "leaderboard"
    const val PROFILE = "profile"
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    data object Home : BottomNavItem("home", "Home", Icons.Default.Home)

    data object Leaderboard : BottomNavItem("leaderboard", "Leaderboard", Icons.Default.Info)

    data object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}
