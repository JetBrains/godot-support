package com.jetbrains.godot.gdscript.dap

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.readAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapScope
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStackFrame
import com.intellij.platform.dap.DapThread
import com.intellij.platform.dap.FrameId
import com.intellij.platform.dap.StackFrameType
import com.intellij.platform.dap.TextPosition
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.PsiDocumentManagerEx
import com.intellij.testFramework.LightVirtualFile
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.TestDisposable
import com.intellij.testFramework.junit5.fixture.projectFixture
import gdscript.dap.remote.GdFrameSourceSelf
import gdscript.dap.remote.GdSelfCheckedFrame
import gdscript.dap.remote.frameSourceSelf
import gdscript.psi.GdFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestApplication
internal class GdFrameSourceSelfTest {
    private val projectFixture = projectFixture(openAfterCreation = true)
    private val project get() = projectFixture.get()

    @ParameterizedTest
    @MethodSource("sourceCases")
    fun testSourceRule(code: String, line: Int, name: String, expected: GdFrameSourceSelf): Unit = timeoutRunBlocking {
        val source = source(code)
        val frame = SourceFrame(source, line, name)
        assertEquals(expected, frameSourceSelf(project, frame))
    }

    @Test
    fun testMissingSourceIsUnavailable(): Unit = timeoutRunBlocking {
        assertEquals(GdFrameSourceSelf.Unavailable, frameSourceSelf(project, SourceFrame(null)))
    }

    @Test
    fun testOtherFileTypeIsUnavailable(): Unit = timeoutRunBlocking {
        val source = LightVirtualFile("frame.txt", "func paused():\n\tpass")
        assertEquals(GdFrameSourceSelf.Unavailable, frameSourceSelf(project, SourceFrame(source)))
    }

    @Test
    fun testBuiltInSourceIsUnavailable(): Unit = timeoutRunBlocking {
        val source = LightVirtualFile("scene.tscn::GDScript_1", "func paused():\n\tpass")
        assertEquals(GdFrameSourceSelf.Unavailable, frameSourceSelf(project, SourceFrame(source)))
    }

    @Test
    fun testUncommittedSourceBlocksEvaluationAndCanBeCheckedAgain(@TestDisposable disposable: Disposable): Unit = timeoutRunBlocking {
        val source = source("func paused():\n\tpass")
        val frame = SourceFrame(source)
        val checked = GdSelfCheckedFrame(frame, { frameSourceSelf(project, frame) })
        withContext(Dispatchers.EDT) {
            (PsiDocumentManager.getInstance(project) as PsiDocumentManagerEx).disableBackgroundCommit(disposable)
            WriteCommandAction.runWriteCommandAction(project) {
                val document = requireNotNull(FileDocumentManager.getInstance().getDocument(source))
                document.insertString(0, "static ")
                assertFalse(PsiDocumentManager.getInstance(project).isCommitted(document))
            }
            assertEquals(GdFrameSourceSelf.Unavailable, frameSourceSelf(project, frame))
            val result = with(unusedSessionContext()) { checked.run { evaluate("self") } }
            assertInstanceOf(DapEvaluationResult.Error::class.java, result)
            assertTrue(frame.evaluations.isEmpty())
            WriteCommandAction.runWriteCommandAction(project) {
                val document = requireNotNull(FileDocumentManager.getInstance().getDocument(source))
                PsiDocumentManager.getInstance(project).commitDocument(document)
            }
        }
        assertEquals(GdFrameSourceSelf.Absent, frameSourceSelf(project, frame))
        val result = with(unusedSessionContext()) { checked.run { evaluate("self") } }
        assertInstanceOf(DapEvaluationResult.Error::class.java, result)
        assertTrue(frame.evaluations.isEmpty())
    }

    private suspend fun source(code: String): VirtualFile = readAction {
        LightVirtualFile("frame.gd", code).also {
            assertInstanceOf(GdFile::class.java, PsiManager.getInstance(project).findFile(it))
            val document = requireNotNull(FileDocumentManager.getInstance().getDocument(it))
            requireNotNull(PsiDocumentManager.getInstance(project).getPsiFile(document))
        }
    }

    private class SourceFrame(
        override val source: VirtualFile?,
        line: Int = 2,
        override val name: String = "paused",
    ) : DapStackFrame {
        val evaluations = mutableListOf<String>()
        override val id = FrameId(1)
        override val type = StackFrameType.Normal
        override val startPosition = TextPosition(line, 1)
        override val endPosition = startPosition
        override val thread: DapThread get() = error("Unexpected thread request")
        override suspend fun DapSessionContext.scopes(): List<DapScope> {
            error("The source rule must block the dump request")
        }
        override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult {
            evaluations.add(expression)
            error("The source rule must block evaluation")
        }
    }

    companion object {
        @JvmStatic
        fun sourceCases(): List<Arguments> {
            fun case(code: String, line: Int, expected: GdFrameSourceSelf, name: String = "paused") =
                Arguments.of(code, line, name, expected)
            val present = GdFrameSourceSelf.Present
            val absent = GdFrameSourceSelf.Absent
            val unavailable = GdFrameSourceSelf.Unavailable
            return listOf(
                case("func paused():\n\tpass", 1, present),
                case("func paused():\n\tpass", 2, present),
                case("static func paused():\n\tpass", 1, absent),
                case("static func paused():\n\tpass", 2, absent),
                case("static var stored = make_value()", 1, absent),
                case("static var stored = (\n\tmake_value())", 2, absent),
                case("@warning_ignore(\"unused_parameter\") static func paused(p): breakpoint", 1, absent),
                case("@warning_ignore(\"unused_private_class_variable\") static var stored = 1", 1, absent),
                case("@warning_ignore(\"unused_parameter\") func paused(p): breakpoint", 1, present),
                case("@warning_ignore(\"unused_parameter\")\nstatic func paused(p): breakpoint", 1, unavailable),
                case("@warning_ignore(\"unused_parameter\") @rpc static func paused(p): breakpoint", 1, absent),
                case("@warning_ignore(\"unused_parameter\") @rpc func paused(p): breakpoint", 1, present),
                case("extends Node\nvar member = 1", 1, absent, "@static_initializer"),
                case("var member = 1", 1, present, "@implicit_new"),
                case("@onready var member = 1", 1, present, "@implicit_ready"),
                case("func paused():\n\tvar fn = func(): return self\n\tpass", 2, absent),
                case("func paused():\n\tvar fn = func():\n\t\treturn self\n\tpass", 3, absent),
                case("func paused():\n\tvar fn = func(): return self\n\tpass", 3, present),
                case("var stored:\n\tget:\n\t\treturn 1", 3, present),
                case("static var stored:\n\tget:\n\t\treturn 1", 3, absent),
                case("class Inner:\n\tfunc paused():\n\t\tpass", 3, present),
                case("class Inner:\n\tstatic func paused():\n\t\tpass", 3, absent),
                case("func paused():\n\n\tpass", 2, unavailable),
                case("func paused():\n\t# comment\n\tpass", 2, unavailable),
                case("func paused():\n\tpass", 0, unavailable),
                case("func paused():\n\tpass", 99, unavailable),
            )
        }
    }
}
