package com.example.flipmatch.data.repository

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.IOException

class PuzzleRepositoryImplTest {
    private lateinit var repository: PuzzleRepository
    private val mockContext: Context = mockk()
    private val mockAssets = mockk<AssetManager>()

    @Before
    fun setup() {
        mockkStatic(Log::class) // Mock Log class
        every { Log.e(any(), any(), any()) } returns 0
        every { mockContext.assets } returns mockAssets
        repository = PuzzleRepositoryImpl(mockContext)
    }

    @Test
    fun `getPuzzles() returns parsed puzzle list when JSON is valid`() {
        // Arrange: Fake JSON content
        val fakeJson =
            """
            [{
              "id": 1,
              "difficulty": "easy",
              "gridSize": 2,
              "images": [
                {
                  "id": 1,
                  "imageRes": null,
                  "emoji": "😊"
                },
                {
                  "id": 2,
                  "imageRes": null,
                  "emoji": "🍌"
                }
              ]
            }]
            """.trimIndent()

        val fakeInputStream = ByteArrayInputStream(fakeJson.toByteArray())
        every { mockAssets.open("puzzles.json") } returns fakeInputStream

        // Act
        val puzzles = repository.getPuzzles()

        // Assert
        assertEquals(1, puzzles.size)
        assertEquals(1, puzzles[0].id)
        assertEquals(2, puzzles[0].images.size)
    }

    @Test
    fun `getPuzzles() returns empty list when JSON file is missing`() {
        // Arrange: Simulate file not found
        every { mockAssets.open("puzzles.json") } throws IOException("File not found")

        // Act
        val puzzles = repository.getPuzzles()

        // Assert
        assertTrue(puzzles.isEmpty())
    }
}
