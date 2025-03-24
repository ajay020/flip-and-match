package com.example.flipmatch.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipmatch.data.model.User
import com.example.flipmatch.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _userData = MutableStateFlow<User?>(null)
        val userData: StateFlow<User?> = _userData

        init {
            fetchUserData()
        }

        private fun fetchUserData() {
            viewModelScope.launch {
                val user = userRepository.getUserData()
                _userData.value = user
            }
        }

        fun logout(onLogoutSuccess: () -> Unit) {
            userRepository.logout()
            onLogoutSuccess() // Call navigation after logout
        }
    }
