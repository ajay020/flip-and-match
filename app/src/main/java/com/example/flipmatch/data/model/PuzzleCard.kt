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
    // Ensure that at least one (image or emoji) is present
    init {
        require(imageRes != null || emoji != null) { "A card must have either an image or an emoji" }
    }
}
