package com.example.flipmatch.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

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

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    data object Home : BottomNavItem("home", "Home", Icons.Default.Home)

    data object Leaderboard : BottomNavItem("leaderboard", "Leaderboard", Icons.Default.Info)

    data object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}
