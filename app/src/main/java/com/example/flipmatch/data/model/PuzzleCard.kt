package com.example.flipmatch.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PuzzleCard(
    val id: Int, // Unique ID to match pairs
    val imageRes: Int? = null, // Drawable resource ID (nullable)
    val emoji: String? = null, // Emoji as a string (nullable)
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false,
) {
    val isEmpty: Boolean
        get() = imageRes == null && emoji == null

    companion object {
        val EMPTY = PuzzleCard(id = -1) // Placeholder for empty slots
    }
}
