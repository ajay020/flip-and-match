package com.example.flipmatch.utils

import android.content.Context
import android.util.Log
import com.example.flipmatch.data.model.Puzzle
import kotlinx.serialization.json.Json
import java.io.IOException

object PuzzleLoader {
    fun loadPuzzles(context: Context): List<Puzzle> =
        try {
            val json =
                context.assets
                    .open("puzzles.json")
                    .bufferedReader()
                    .use { it.readText() }
            Json.decodeFromString(json)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("PuzzleLoader", "Error loading puzzles: ${e.message}", e)
            emptyList() // Return empty list if there's an error
        }
}
