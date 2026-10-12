package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.xdebugger.DefaultDapXSuspendContext
import com.intellij.platform.dap.xdebugger.DefaultDapXStackFrame
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import gdscript.dap.remote.GdDapPresentationFactory
import gdscript.dap.remote.GdSelfCheckedFrame
import kotlinx.coroutines.supervisorScope
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

@TestApplication
class GdWatchStopTest {
    private val projectFixture = projectFixture()

    @Test
    fun testEachSuspendContextBindsItsOwnFrames(): Unit = timeoutRunBlocking {
        supervisorScope {
            val factory = GdDapPresentationFactory(projectFixture.get(), null)
            val executor = ControlledExecutor(this)
            val raw = TestFrame { error("The binding check must not evaluate") }
            val thread = raw.thread
            val first = factory.createSuspendContext(executor, listOf(thread), thread) as DefaultDapXSuspendContext
            val second = factory.createSuspendContext(executor, listOf(thread), thread) as DefaultDapXSuspendContext
            assertNotSame(first.factory, second.factory)
            fun frame(context: DefaultDapXSuspendContext): GdSelfCheckedFrame =
                (context.factory.createStackFrame(executor, thread, raw) as DefaultDapXStackFrame).frame as GdSelfCheckedFrame
            val firstFrame = frame(first)
            val secondFrame = frame(second)
            assertNotNull(firstFrame.watchStop)
            assertNotNull(secondFrame.watchStop)
            assertNotSame(firstFrame.watchStop, secondFrame.watchStop)
            assertSame(firstFrame.watchStop, frame(first).watchStop)
            assertFalse(firstFrame.watchStop!!.isCurrent())
            assertFalse(secondFrame.watchStop!!.isCurrent())
        }
    }
}
