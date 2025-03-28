package com.example.flipmatch.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flipmatch.ui.components.BottomNavBar
import com.example.flipmatch.ui.navigation.AppNavHost
import com.example.flipmatch.utils.Routes

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = currentRoute !in setOf(Routes.GAME, Routes.SETTINGS)) {
                BottomNavBar(navController)
            }
        },
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            rootNavController = rootNavController,
            modifier = modifier.padding(innerPadding),
        )
    }
}
