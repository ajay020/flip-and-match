package com.example.flipmatch.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
        topBar = {
            if (currentRoute == Routes.HOME) {
                MainTopBar()
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = currentRoute !in setOf(Routes.GAME)) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar() {
    CenterAlignedTopAppBar(
        title = { Text("Flip & Match") },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    )
}
