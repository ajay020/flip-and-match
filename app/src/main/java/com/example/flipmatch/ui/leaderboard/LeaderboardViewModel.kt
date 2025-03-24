package com.example.flipmatch.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipmatch.data.model.User
import com.example.flipmatch.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _leaderboard = MutableStateFlow<List<User>>(emptyList())
        val leaderboard: StateFlow<List<User>> = _leaderboard

        init {
            fetchLeaderboard()
        }

        private fun fetchLeaderboard() {
            viewModelScope.launch {
                val users = userRepository.getTopUsersWithCurrent()
                _leaderboard.value = users
            }
        }
    }
