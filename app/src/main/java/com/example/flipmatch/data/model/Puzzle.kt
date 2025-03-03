package com.example.flipmatch.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Puzzle(
    val id: Int,
    val difficulty: String,
    val gridSize: Int, // Example: 4 means a 4x4 grid
    val images: List<PuzzleCard>, // List of images in the puzzle
)
