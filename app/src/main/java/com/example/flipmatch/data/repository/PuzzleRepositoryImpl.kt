package com.example.flipmatch.data.repository

import android.content.Context
import com.example.flipmatch.data.model.Puzzle
import com.example.flipmatch.utils.PuzzleLoader

class PuzzleRepositoryImpl(
    private val context: Context,
) : PuzzleRepository {
    override fun getPuzzles(): List<Puzzle> = PuzzleLoader.loadPuzzles(context)
}
