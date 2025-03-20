package com.example.flipmatch.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test

class PuzzleLoaderTest {
    @Test
    fun loadPuzzlesTest() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        PuzzleLoader.loadPuzzles(context, "testPuzzles.json")
    }

    @Test(expected = Exception::class)
    fun loadPuzzlesTest_invalid_json_exception() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        PuzzleLoader.loadPuzzles(context, "malformed.json")
    }

    @Test
    fun loadPuzzlesTest_valid_json_expect_count() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val puzzleList = PuzzleLoader.loadPuzzles(context, "testPuzzles.json")
        assertEquals(2, puzzleList.size)
    }
}
