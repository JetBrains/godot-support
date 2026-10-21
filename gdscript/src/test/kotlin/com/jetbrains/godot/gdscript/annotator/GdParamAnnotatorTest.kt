package com.jetbrains.godot.gdscript.annotator

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.TestModeFlags
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.GdScriptBundle
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * `GdParamAnnotator` used to be blind to SDK/engine ancestors: `symbol.gdPsiSourceElement?.parent
 * ?: return` bailed immediately for any SDK-backed callee, so argument-count/type validation never
 * ran for a call to an SDK method or an SDK class's constructor. Verifies the cast-free rewrite
 * (dispatch on `symbol.kind` + `gdSignature`/`listConstructorSymbols`) fixes this for both
 * `ClassName.new(...)` and bare `ClassName(...)` constructor-call syntax, using the real bundled
 * `Vector2` SDK doc data (4 constructor overloads, max arity 2 - see `sdk/4.5.0/Vector2.xml`).
 *
 * Uses `checkHighlighting`/inline `<error>` markers (matching `GdRefIdAnnotatorTest`'s established,
 * working pattern). Each fixture starts with a harmless "warm-up" statement that also references an
 * SDK class symbol before the statement under test - confirmed empirically that querying an SDK
 * class's symbol scope for the first time in a fresh file can otherwise hit a pre-existing, unrelated
 * `PolySymbolHighlightingAnnotator` range-computation crash (a cold-cache issue in that scope's own
 * machinery, reproduced and root-caused via bisection; nothing to do with `GdParamAnnotator` itself -
 * the crash occurs identically whether or not this fix is applied).
 */
@TestApplication
class GdParamAnnotatorTest {
    private val gd: GdSdkFixture by gdSdkFixture("highlighting")

    @BeforeEach
    fun preserveAnnotatorSeverity() {
        TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, gd.testRootDisposable)
    }

    @Test
    fun testTooManyArgumentsFlaggedForSdkConstructorViaNew() {
        gd.fixture.configureByText(
            "a.gd", """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = <error descr="Too many arguments">Vector2.new(1, 2, 3)</error>
        """.trimMargin()
        )
        gd.fixture.checkHighlighting(false, false, false)
    }

    @Test
    fun testTooManyArgumentsFlaggedForBareSdkConstructorCall() {
        gd.fixture.configureByText(
            "a.gd", """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = <error descr="Too many arguments">Vector2(1, 2, 3)</error>
        """.trimMargin()
        )
        gd.fixture.checkHighlighting(false, false, false)
    }

    @Test
    fun testValidTwoArgSdkConstructorCallNotFlagged() {
        gd.fixture.configureByText(
            "a.gd", """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = Vector2.new(1.0, 2.0)
        """.trimMargin()
        )
        gd.fixture.checkHighlighting(false, false, false)
    }

    @Test
    fun testChangeTypeFixUpdatesAnOrdinaryUserDeclaredParameter() {
        gd.fixture.configureByText(
            "a.gd", """
            |func take_node(value: Node):
            |	pass
            |
            |func caller():
            |	take_node(<caret>5)
        """.trimMargin()
        )

        val errors = gd.fixture.doHighlighting().filter { it.severity === HighlightSeverity.ERROR }
        assertFalse(errors.isEmpty(), "expected a type-mismatch error for the call")

        val fix = gd.fixture.findSingleIntention(GdScriptBundle.message("intention.name.change.type.to", "int"))
        gd.fixture.launchAction(fix)

        gd.fixture.checkResult(
            """
            |func take_node(value: int):
            |	pass
            |
            |func caller():
            |	take_node(5)
        """.trimMargin()
        )
    }
}
