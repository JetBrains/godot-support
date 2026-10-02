package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.testFramework.common.timeoutRunBlocking
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rd.util.lifetime.isAlive
import com.jetbrains.rider.test.shared.constants.TeamCityTags
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.yield
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

@Tag(TeamCityTags.Plugins.Godot.General)
@Timeout(30)
class GodotInEditorDebugCallbackTest {
    private val projectLifetime = LifetimeDefinition()
    private val cleanupLifetime = projectLifetime.lifetime.createNested()
    private val cleanupFinished = CompletableDeferred<Unit>()
    private val cleanupCount = AtomicInteger()
    private var reportedError: Throwable? = null
    private var startedCalls = 0
    private var delegateFailure: IllegalStateException? = null
    private val previousCallback = object : ProgramRunner.Callback {
        override fun processNotStarted(error: Throwable?) {
            reportedError = error
            delegateFailure?.let { throw it }
        }

        override fun processStarted(descriptor: RunContentDescriptor?) {
            assertNull(descriptor)
            startedCalls++
        }
    }
    private val callback = createGodotInEditorDebugCallback(cleanupLifetime, previousCallback) {
        yield()
        cleanupCount.incrementAndGet()
        cleanupFinished.complete(Unit)
    }

    @AfterEach
    fun tearDown() = timeoutRunBlocking {
        projectLifetime.terminate()
        cleanupFinished.await()
    }

    @Test
    fun `startup failure triggers cleanup without a process handler`() = timeoutRunBlocking {
        val failure = IllegalStateException("Worker creation failed")

        callback.processNotStarted(failure)
        cleanupFinished.await()

        assertEquals(1, cleanupCount.get())
        assertSame(failure, reportedError)
        assertTrue(projectLifetime.lifetime.isAlive)
        assertFalse(cleanupLifetime.lifetime.isAlive)
    }

    @Test
    fun `startup cancellation completes suspending cleanup`() = timeoutRunBlocking {
        val cancellation = CancellationException("Startup canceled")

        callback.processNotStarted(cancellation)
        cleanupFinished.await()

        assertEquals(1, cleanupCount.get())
        assertSame(cancellation, reportedError)
    }

    @Test
    fun `a null descriptor triggers cleanup and forwards the callback`() = timeoutRunBlocking {
        callback.processStarted(null)
        cleanupFinished.await()

        assertEquals(1, cleanupCount.get())
        assertEquals(1, startedCalls)
    }

    @Test
    fun `lifetime termination triggers cleanup without a startup failure`() = timeoutRunBlocking {
        assertTrue(cleanupLifetime.lifetime.isAlive)

        cleanupLifetime.terminate()
        cleanupFinished.await()

        assertEquals(1, cleanupCount.get())
        assertTrue(projectLifetime.lifetime.isAlive)
    }

    @Test
    fun `repeated termination triggers cleanup once`() = timeoutRunBlocking {
        callback.processNotStarted(null)
        callback.processStarted(null)
        cleanupLifetime.terminate()
        projectLifetime.terminate()
        cleanupFinished.await()

        assertEquals(1, cleanupCount.get())
    }

    @Test
    fun `a delegate failure does not prevent cleanup`() = timeoutRunBlocking {
        val failure = IllegalStateException("Callback failed")
        delegateFailure = failure

        assertSame(failure, assertFailsWith<IllegalStateException> { callback.processNotStarted(null) })
        cleanupFinished.await()

        assertEquals(1, cleanupCount.get())
    }
}
