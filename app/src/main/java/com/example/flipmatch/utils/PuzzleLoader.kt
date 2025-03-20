package com.example.flipmatch.utils

import android.content.Context
import android.util.Log
import com.example.flipmatch.data.model.Puzzle
import kotlinx.serialization.json.Json
import java.io.IOException

object PuzzleLoader {
    fun loadPuzzles(
        context: Context,
        fileName: String = "puzzles.json",
    ): List<Puzzle> =
        try {
            val json =
                context.assets
                    .open(fileName)
                    .bufferedReader()
                    .use { it.readText() }
            Json.decodeFromString(json)
        } catch (e: IOException) {
            Log.e("PuzzleLoader", "Error loading puzzles: ${e.message}", e)
            emptyList() // Return empty list if there's an error
        }
}
