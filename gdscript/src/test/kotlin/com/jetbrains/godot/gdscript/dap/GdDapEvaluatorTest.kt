package com.jetbrains.godot.gdscript.dap

import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapStackFrame
import com.intellij.platform.dap.TextPosition
import com.intellij.platform.dap.xdebugger.DefaultDapXDebuggerPresentationFactory
import com.intellij.platform.testFramework.junit5.codeInsight.fixture.codeInsightFixture
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.PlatformTestUtil.waitWithEventsDispatching
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.moduleFixture
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.intellij.testFramework.junit5.fixture.tempPathFixture
import com.intellij.xdebugger.evaluation.ExpressionInfo
import gdscript.dap.remote.GdDapEvaluator
import gdscript.dap.remote.GdHoverRangeOutcome
import gdscript.dap.remote.GdSelfCheckedFrame
import gdscript.dap.remote.hoverRangeAtOffset
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.concurrency.Promise
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junitpioneer.jupiter.cartesian.ArgumentSets
import org.junitpioneer.jupiter.cartesian.CartesianTest
import java.lang.reflect.Proxy

/**
 * Checks which source text the evaluator selects at a hover position.
 * A range identifies that text by its start and end offsets.
 * Checks the display text, rewritten node paths, scope limits, and side-effect permission.
 * Uses fake frames and sends no DAP requests.
 * These tests check expression selection, not evaluation results or request contexts.
 */
@TestApplication
class GdDapEvaluatorTest {
    private val tempPath = tempPathFixture()
    private val projectFixture = projectFixture(openAfterCreation = true)
    @Suppress("unused") // The code insight fixture needs a module in the project.
    private val moduleFixture = projectFixture.moduleFixture(tempPath)
    private val codeInsightFixture by codeInsightFixture(projectFixture, tempPath)
    private val project get() = projectFixture.get()

    @CartesianTest(name = "{index}: {0}, sideEffectsAllowed={1}")
    @CartesianTest.MethodFactory("sharedCases")
    fun testRange(case: RangeCase, allowed: Boolean) = timeoutRunBlocking {
        withContext(Dispatchers.EDT) { checkRange(case, allowed) }
    }

    @ParameterizedTest(name = "{index}: {0}, sideEffectsAllowed={1}")
    @MethodSource("flaggedCases")
    fun testFlaggedRange(case: RangeCase, allowed: Boolean) = timeoutRunBlocking {
        withContext(Dispatchers.EDT) { checkRange(case, allowed) }
    }

    @Suppress("DEPRECATION", "ForbiddenInSuspectContextMethod")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("frameCases")
    fun testFrameRange(case: FrameCase) = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val frameFile = codeInsightFixture.configureByText("hover.gd", case.code)
            val hoverFile = if (case.otherFile) {
                codeInsightFixture.addFileToProject("other.gd", "func other():\n\tprint(foreign)")
            } else frameFile
            val document = requireNotNull(PsiDocumentManager.getInstance(project).getDocument(hoverFile))
            val offset = document.text.indexOf(case.part).also { require(it >= 0) }
            val source = if (case.noSource) null else frameFile.virtualFile
            val frame = fakeFrame(source, case.frameLine, case.frameName)
            val checked = checkedFrame(frame)
            val outcome = ReadAction.compute<GdHoverRangeOutcome, RuntimeException> {
                hoverRangeAtOffset(project, checked, document, offset, false)
            }
            assertEquals(if (case.selected == null) GdHoverRangeOutcome.OutOfFrameScope else GdHoverRangeOutcome.PlatformRule,
                         outcome, "The range outcome for $case")
            assertSelectedRange(checked, document, offset, false, case.selected, case.selected, case.toString())
        }
    }

    private fun fakeFrame(source: VirtualFile?, line: Int, name: String): DapStackFrame =
        Proxy.newProxyInstance(DapStackFrame::class.java.classLoader, arrayOf(DapStackFrame::class.java)) { _, method, _ ->
            when {
                method.name.startsWith("getId-") -> 1
                method.name == "getSource" -> source
                method.name == "getStartPosition" -> TextPosition(line, 1)
                method.name == "getName" -> name
                else -> error("The range check must not call ${method.name}")
            }
        } as DapStackFrame

    @Suppress("DEPRECATION", "ForbiddenInSuspectContextMethod")
    private fun checkRange(case: RangeCase, allowed: Boolean) {
        val (code, part, selected, expression) = case
        val file = codeInsightFixture.configureByText("hover.gd", code)
        val document = requireNotNull(PsiDocumentManager.getInstance(project).getDocument(file))
        val offset = code.indexOf(part).also { require(it >= 0) } + if (part.startsWith(".") || part.startsWith("(")) 1 else 0
        val frame = checkedFrame(fakeFrame(file.virtualFile, 1, "test"))
        val outcome = ReadAction.compute<GdHoverRangeOutcome, RuntimeException> {
            hoverRangeAtOffset(project, frame, document, offset, allowed)
        }
        when (case.expectedOutcome) {
            RangeOutcome.NoRange -> assertEquals(GdHoverRangeOutcome.NoRange, outcome, "The range outcome for '$part'")
            RangeOutcome.Range -> assertInstanceOf(GdHoverRangeOutcome.Range::class.java, outcome)
            RangeOutcome.PlatformRule -> assertEquals(GdHoverRangeOutcome.PlatformRule, outcome, "The range outcome for '$part'")
        }
        assertSelectedRange(frame, document, offset, allowed, selected, expression, part)
    }

    private fun assertSelectedRange(
        frame: GdSelfCheckedFrame, document: Document, offset: Int, allowed: Boolean,
        selected: String?, expression: String?, label: String,
    ) {
        val promise: Promise<ExpressionInfo?> = GdDapEvaluator(UnusedExecutor, DefaultDapXDebuggerPresentationFactory(null), frame)
            .getExpressionInfoAtOffsetAsync(project, document, offset, allowed)
        waitWithEventsDispatching({ "The hover range did not complete for $label" },
                                  { promise.state != Promise.State.PENDING }, 20)
        val info = promise.blockingGet(0)
        assertEquals(selected, info?.let { document.getText(it.textRange) }, "The source range for '$label'")
        if (info != null) {
            assertEquals(expression, info.expressionText ?: selected, "The evaluation text for '$label'")
            if (expression != selected) assertEquals(selected, info.displayText, "The display text for '$label'")
        }
    }

    private fun checkedFrame(frame: DapStackFrame) = GdSelfCheckedFrame(frame, {
        error("The hover range must not check the Members dump or evaluate")
    })

    data class FrameCase(
        val code: String,
        val frameLine: Int,
        val part: String,
        val selected: String?,
        val otherFile: Boolean = false,
        val noSource: Boolean = false,
        val frameName: String = "paused",
    ) {
        override fun toString(): String = "frame line $frameLine, $part in ${code.replace("\n", "\\n")}"
    }

    enum class RangeOutcome { NoRange, Range, PlatformRule }

    data class RangeCase(
        val code: String,
        /** Selects the first occurrence in [code]. A leading `.` or `(` moves the offset forward by one character. */
        val part: String,
        val selected: String?,
        val expression: String? = selected,
        val expectedOutcome: RangeOutcome = when {
            selected == null -> RangeOutcome.NoRange
            expression != selected -> RangeOutcome.Range
            else -> RangeOutcome.PlatformRule
        },
    ) {
        override fun toString(): String = "${part} in ${code.replace("\n", "\\n")}"
    }

    companion object {
        @JvmStatic
        @Suppress("unused") // The method factory annotation uses this name.
        fun sharedCases(): ArgumentSets = ArgumentSets.argumentsForFirstParameter(
            listOf(
                RangeCase("func test(param):\n\tvar local = param", "local", "local"),
                RangeCase("func test(param):\n\tvar local = param", "param", "param"),
                RangeCase("var health = 10", "health", "health"),
                RangeCase("func test():\n\tprint(self.health)", "self", "self"),
                RangeCase("func test():\n\tprint(self.health)", "health", "self.health"),
                RangeCase("func test():\n\tprint(a.b.c)", "a", "a"),
                RangeCase("func test():\n\tprint(a.b.c)", "b", "a.b"),
                RangeCase("func test():\n\tprint(a.b.c)", "c)", "a.b.c"),
                RangeCase("func test():\n\tprint(Vector2.ZERO)", "Vector2", "Vector2"),
                RangeCase("var health = 10", "var", null),
                RangeCase("func test():\n\tpass", "func", null),
                RangeCase("func test():\n\tif true:\n\t\tpass", "if", null),
                RangeCase("var x = true", "true", null),
                RangeCase("var x = null", "null", null),
                RangeCase("var x = 42", "42", null),
                RangeCase("var x = \"words\"", "words", null),
                RangeCase("var x = 1 # words", "words", null),
                RangeCase("var x: Vector2", "Vector2", null),
                RangeCase("func test(param: Vector2):\n\tpass", "Vector2", null),
                RangeCase("func test() -> Vector2:\n\tpass", "Vector2", null),
                RangeCase("var x = y as Vector2", "Vector2", null),
                RangeCase("var x = y is Vector2", "Vector2", null),
                RangeCase("extends Node", "Node", null),
                RangeCase($$"func test():\n\tprint($Player)", $$"$Player", $$"$Player", "get_node_or_null(\"Player\")"),
                RangeCase("func test():\n\tprint($\"A/B\")", "$\"A/B\"", "$\"A/B\"", "get_node_or_null(\"A/B\")"),
                RangeCase("func test():\n\tprint(%Label)", "%Label", "%Label", "get_node_or_null(\"%Label\")"),
                RangeCase("func test():\n\tprint(%\"Score Label\")", "Score Label", "%\"Score Label\"",
                          "get_node_or_null(\"%Score Label\")"),
                RangeCase("func test():\n\tprint(%\"Score Label\".health)", "health", "%\"Score Label\".health",
                          "get_node_or_null(\"%Score Label\").health"),
                RangeCase("func test():\n\tprint($'A\\vB')", "A\\vB", "$'A\\vB'", "get_node_or_null('A\\vB')"),
                RangeCase("func test():\n\tprint($'A\"B')", "A\"B", "$'A\"B'", "get_node_or_null('A\"B')"),
                RangeCase($$"func test():\n\tprint($Player.health)", "Player", $$"$Player", "get_node_or_null(\"Player\")"),
                RangeCase($$"func test():\n\tprint($Player.health)", "health", $$"$Player.health",
                          "get_node_or_null(\"Player\").health"),
                RangeCase($$"func test():\n\tprint($A.b.c)", "b", $$"$A.b", "get_node_or_null(\"A\").b"),
                RangeCase("func test():\n\tprint($\"A\\\"B\")", "A\\\"B", "$\"A\\\"B\"", "get_node_or_null(\"A\\\"B\")"),
                RangeCase("func test():\n\tprint($\"A\\\\B\")", "A\\\\B", "$\"A\\\\B\"", "get_node_or_null(\"A\\\\B\")"),
                RangeCase("func test():\n\tprint(%'A\\'B')", "A\\'B", "%'A\\'B'", "get_node_or_null('%A\\'B')"),
            )
        ).argumentsForNextParameter(false, true)

        @JvmStatic
        @Suppress("unused") // The method source annotation uses this name.
        fun frameCases(): List<FrameCase> = listOf(
            FrameCase("func paused():\n\tvar local = 1\n\tprint(local)", 2, "local", "local"),
            FrameCase("var member = 1\nfunc paused():\n\tprint(member)", 3, "member", "member"),
            FrameCase("func paused():\n\tpass\nfunc other():\n\tprint(foreign)", 2, "foreign", null),
            FrameCase("func paused():\n\tpass\nclass Inner:\n\tvar innerValue = 1", 2, "innerValue", null),
            FrameCase("func paused():\n\tpass", 2, "foreign", null, otherFile = true),
            FrameCase("var member = 1\nfunc other():\n\tprint(foreign)", 1, "foreign", null),
            FrameCase("func paused():\n\tvar fn = func(): return inside\n\tprint(fn)", 3, "inside", "inside"),
            FrameCase("func paused():\n\tvar fn = func(): return inside\n\tprint(fn)", 2, "fn)", null),
            FrameCase("func paused():\n\tvar before = 1\n\tvar fn = func(): return inside", 3, "before", null),
            FrameCase("func paused():\n\tvar fn = func():\n\t\treturn inside\n\tprint(fn)", 3, "fn)", null),
            FrameCase("static func paused():\n\tvar fn = func():\n\t\treturn inside\n\tprint(fn)", 3, "inside", null),
            FrameCase("static func paused():\n\tprint(local)\nvar member = 1", 2, "local", null),
            FrameCase("static var stored = make_value\nvar member = 1", 1, "member", null),
            FrameCase("extends Node\nstatic var stored = make_value\nvar member = 1", 1, "member", null,
                      frameName = "@static_initializer"),
            FrameCase("extends Node\nstatic var stored = make_value\nvar member = 1", 1, "member", "member",
                      frameName = "other"),
            FrameCase("static var stored = (\n\tmake_value)\nvar member = 1", 2, "member", null),
            FrameCase("static var stored:\n\tget:\n\t\treturn member\nvar member = 1", 3, "member", null),
            FrameCase("var stored:\n\tget:\n\t\treturn member\nvar member = 1", 3, "member", "member"),
            FrameCase("var stored:\n\tget:\n\t\treturn member\n\tset(value):\n\t\tprint(value)", 3, "value)", null),
            FrameCase("var stored:\n\tset(value):\n\t\tprint(value)", 3, "value)", "value"),
            FrameCase("var stored:\n\tget = read_stored\nfunc read_stored():\n\treturn member", 2, "member", null),
            FrameCase("static func read_stored():\n\treturn member\nvar stored:\n\tget = read_stored", 2, "member", null),
            FrameCase("func paused():\n\tprint(local)", 1, "local", "local"),
            FrameCase("func paused():\n\tprint(local)", 2, "local", "local"),
            FrameCase("func paused():\n\tprint(\n\t\tlocal)", 3, "local", "local"),
            FrameCase("func paused():\n\tprint(local)", 99, "local", null),
            FrameCase("func paused():\n\tprint(local)", 2, "local", null, noSource = true),
            FrameCase("func paused():\n\n\tprint(local)", 2, "local", null),
            FrameCase("@warning_ignore(\"unused_parameter\")\nfunc paused(p):\n\tprint(p)", 1, "p)", null),
        )

        @JvmStatic
        fun flaggedCases(): List<Arguments> = listOf(
            Arguments.of(RangeCase("func test():\n\tprint(a.call())", "call", null, expectedOutcome = RangeOutcome.PlatformRule), false),
            Arguments.of(RangeCase("func test():\n\tprint(a.call())", "call", "a.call()"), true),
            Arguments.of(RangeCase($$"func test():\n\tprint($Player.play())", "play", null), false),
            Arguments.of(RangeCase($$"func test():\n\tprint($Player.play())", "play", $$"$Player.play()",
                                   "get_node_or_null(\"Player\").play()"), true),
            Arguments.of(RangeCase($$"func test():\n\tprint($Player.play().x)", ".x", null,
                                   expectedOutcome = RangeOutcome.PlatformRule), false),
            Arguments.of(RangeCase($$"func test():\n\tprint($Player.mutate(child as Node).health)", "child", "child"), false),
            Arguments.of(RangeCase($$"func test():\n\tprint($Player.mutate(child as Node).health)", "Node", null), false),
            Arguments.of(RangeCase($$"func test():\n\tprint($A.f(x).b)", "(x)", "x"), false),
            Arguments.of(RangeCase($$"func test():\n\tprint($A[i].b)", ".b", null,
                                   expectedOutcome = RangeOutcome.PlatformRule), false),
        )
    }

    private object UnusedExecutor : DapSessionExecutor {
        override fun post(block: suspend DapSessionContext.() -> Unit): Unit = error("No DAP request is expected")
        override fun <T> postAsync(block: suspend DapSessionContext.() -> T): Deferred<T> = error("No DAP request is expected")
    }
}
