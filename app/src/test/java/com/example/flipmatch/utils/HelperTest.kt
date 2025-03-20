package com.example.flipmatch.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class HelperTest {
    @Test
    fun textIsEven() {
        // Arrange
        val helper = Helper()

        // Act
        val result = helper.isEven(2)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun textIsEven_inputOdd_returnFalse() {
        // Arrange
        val helper = Helper()

        // Act
        val result = helper.isEven(1)

        // Assert
        assertEquals(false, result)
    }
}
