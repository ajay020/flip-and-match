package com.example.flipmatch.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun ProfileScreen(
    navigateToSetting: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit,
) {
    val user by profileViewModel.userData.collectAsState()

    Scaffold(
        topBar = {
            ProfileTopAppBar(
                modifier =
                    Modifier
                        .padding(16.dp),
                onLogout = {
                    profileViewModel.logout {
                        onLogout()
                    }
                },
                onSettings = {
                    navigateToSetting()
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            user?.let {
                Image(
                    painter = rememberAsyncImagePainter(it.profileImageUrl),
                    contentDescription = "Profile Image",
                    modifier =
                        Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Name: ${it.name}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Score: ${it.totalScore}", fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onSettings: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "Profile") },
        actions = {
            IconButton(onClick = {
                onLogout()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
            }

            IconButton(onClick = {
                onSettings()
            }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "settings")
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PfofilePreview() {
    ProfileTopAppBar(
        onLogout = { },
    ) { }
}
