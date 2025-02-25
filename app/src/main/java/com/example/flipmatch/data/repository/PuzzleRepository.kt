package com.example.flipmatch.data.repository

import android.content.Context
import com.example.flipmatch.data.model.Puzzle
import com.example.flipmatch.utils.PuzzleLoader

class PuzzleRepository(
    private val context: Context,
) {
    fun getPuzzles(): List<Puzzle> = PuzzleLoader.loadPuzzles(context)
}
