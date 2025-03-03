package com.example.flipmatch.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.flipmatch.data.model.User
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel = hiltViewModel()) {
    val leaderboard by viewModel.leaderboard.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        itemsIndexed(leaderboard) { index, user ->
            LeaderboardItem(
                user,
                index + 1,
                isCurrentUser = user.uid == FirebaseAuth.getInstance().currentUser?.uid,
            )
        }
    }
}

@Composable
fun LeaderboardItem(
    user: User,
    rank: Int,
    isCurrentUser: Boolean,
) {
    val backgroundColor = if (isCurrentUser) Color.Yellow.copy(alpha = 0.3f) else Color.Transparent

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "$rank.", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = user.name, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Score: ${user.totalScore}", fontWeight = FontWeight.SemiBold)
    }
}
