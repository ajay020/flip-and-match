package com.example.flipmatch.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    navigateToProfile: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val darkMode by viewModel.darkMode.collectAsState()
    val sound by viewModel.sound.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = {
            SettingsTopAppBar(
                navigateToProfile = {
                    navigateToProfile()
                },
            )
        },
    ) {
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            SettingItem(
                title = "Dark Mode",
                checked = darkMode,
                onToggle = {
                    viewModel.toggleDarkMode(it)
                },
            )

            SettingItem(
                title = "Sound",
                checked = sound,
                onToggle = {
                    viewModel.toggleSound(it)
                },
            )

            SettingItem(
                title = "Notifications",
                checked = notifications,
                onToggle = {
                    viewModel.toggleNotifications(it)
                },
            )
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
        )
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppBar(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "Settings") },
        navigationIcon = {
            IconButton(onClick = {
                navigateToProfile()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
//    SettingsScreen()
}
