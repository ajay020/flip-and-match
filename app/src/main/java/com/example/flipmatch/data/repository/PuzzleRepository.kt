package com.example.flipmatch.data.repository

import com.example.flipmatch.data.model.Puzzle

interface PuzzleRepository {
    fun getPuzzles(): List<Puzzle>
}
