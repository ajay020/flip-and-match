package com.example.flipmatch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    val scheduler = TestCoroutineScheduler()

    override fun starting(description: Description?) {
        Dispatchers.setMain(dispatcher) // ✅ Replace Main dispatcher
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain() // ✅ Reset after tests
    }
}
