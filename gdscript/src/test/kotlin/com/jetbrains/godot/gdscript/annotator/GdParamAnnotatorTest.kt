package com.jetbrains.godot.gdscript.annotator

import com.intellij.testFramework.TestModeFlags
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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
@RunWith(JUnit4::class)
class GdParamAnnotatorTest : GdTestCaseWithSdk("highlighting") {

    override fun setUp() {
        super.setUp()
        TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, testRootDisposable)
    }

    @Test
    fun testTooManyArgumentsFlaggedForSdkConstructorViaNew() {
        myFixture.configureByText(
            "a.gd", """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = <error descr="Too many arguments">Vector2.new(1, 2, 3)</error>
        """.trimMargin()
        )
        myFixture.checkHighlighting(false, false, false)
    }

    @Test
    fun testTooManyArgumentsFlaggedForBareSdkConstructorCall() {
        myFixture.configureByText(
            "a.gd", """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = <error descr="Too many arguments">Vector2(1, 2, 3)</error>
        """.trimMargin()
        )
        myFixture.checkHighlighting(false, false, false)
    }

    @Test
    fun testValidTwoArgSdkConstructorCallNotFlagged() {
        myFixture.configureByText(
            "a.gd", """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = Vector2.new(1.0, 2.0)
        """.trimMargin()
        )
        myFixture.checkHighlighting(false, false, false)
    }
}
