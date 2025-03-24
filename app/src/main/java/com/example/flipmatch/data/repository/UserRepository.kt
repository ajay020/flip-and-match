package com.example.flipmatch.data.repository

import com.example.flipmatch.data.model.User

interface UserRepository {
    suspend fun getUserData(): User?

    fun logout()

    suspend fun updateUserScore(
        uid: String,
        puzzleType: String,
        newScore: Int,
    ): Boolean

    suspend fun getTopUsersWithCurrent(): List<User>
}
