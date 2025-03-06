package com.example.flipmatch.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.flipmatch.data.model.User
import com.example.flipmatch.ui.theme.AppTypography

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
                modifier = Modifier,
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
        MainScreenContent(
            modifier = Modifier.padding(paddingValues),
            user = user,
        )
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    user: User? = null,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        user?.let {
            // Profile Image with Border
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(it.profileImageUrl),
                    contentDescription = "Profile Image",
                    modifier =
                        Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = it.name,
                style = AppTypography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Total Score
            Text(
                text = "Total Score: ${it.totalScore}",
                style = AppTypography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card for Best Scores
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Best Scores",
                        style = AppTypography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Score Rows
                    BestScoreItem("Easy", it.bestScores["easy"] ?: 0)
                    BestScoreItem("Medium", it.bestScores["medium"] ?: 0)
                    BestScoreItem("Hard", it.bestScores["hard"] ?: 0)
                }
            }
        }
    }
}

@Composable
fun BestScoreItem(
    level: String,
    score: Int,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = level, style = AppTypography.bodyMedium, fontWeight = FontWeight.Medium)
        Text(text = "$score", style = AppTypography.bodyMedium, fontWeight = FontWeight.Bold)
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
                Icon( modifier = Modifier.size( 32.dp),
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout"
                )
            }

            IconButton(onClick = {
                onSettings()
            }) {
                Icon(
                    modifier = Modifier.size( 32.dp),
                    imageVector = Icons.Default.Settings,
                    contentDescription = "settings"
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PfofilePreview() {
//    val user =
//        User(
//            name = "John Doe",
//            profileImageUrl = "https://example.com/profile.jpg",
//            totalScore = 100,
//            bestScores = mapOf("easy" to 50, "medium" to 75, "hard" to 100),
//        )
//
//    MainScreenContent(
//        user = user,
//    )

    ProfileTopAppBar(
        onLogout = { },
    ) { }
}
