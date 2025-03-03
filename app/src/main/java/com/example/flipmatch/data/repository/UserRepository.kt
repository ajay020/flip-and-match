package com.example.flipmatch.data.repository

import com.example.flipmatch.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserData(): Flow<User?>

    fun logout()

    fun updateUserScore(
        uid: String,
        puzzleType: String,
        newScore: Int,
    ): Flow<Boolean>

    fun getTopUsers(): Flow<List<User>>
}
