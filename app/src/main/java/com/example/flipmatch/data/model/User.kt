package com.example.flipmatch.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    val totalScore: Int = 0,
    val bestScores: Map<String, Int> = emptyMap(),
)
