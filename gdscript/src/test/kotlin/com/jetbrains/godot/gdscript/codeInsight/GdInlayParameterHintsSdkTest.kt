package com.jetbrains.godot.gdscript.codeInsight

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * `GdInlayParameterHintProvider` used to be blind to SDK/engine callees: `symbol.gdPsiSourceElement
 * ?.parent ?: return` bailed for any SDK-backed method/constructor, so no inline parameter-name
 * hints ever showed for a call to an SDK/engine method or an SDK class's constructor. Verifies the
 * cast-free rewrite (dispatch on `symbol.kind` + `gdSignature`) fixes this, using the real bundled
 * `Vector2` SDK doc data. Each fixture starts with a harmless "warm-up" statement referencing the
 * same SDK class first - see `GdParamAnnotatorTest` for why (a cold-cache issue in the SDK class
 * symbol scope's own machinery, unrelated to this provider).
 */
@RunWith(JUnit4::class)
class GdInlayParameterHintsSdkTest : GdTestCaseWithSdk("codeInsight") {

    @Test
    fun testInlayHintsForSdkMethodCall() {
        val code = """
            |func f(v: Vector2):
            |	var warmup = Vector2.new()
            |	v.angle_to(<hint text="to:"/>warmup)
        """.trimMargin()
        myFixture.configureByText("test.gd", code)
        myFixture.testInlays()
    }

    @Test
    fun testInlayHintsForSdkConstructorViaNew() {
        val code = """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = Vector2.new(<hint text="x:"/>1.0, <hint text="y:"/>2.0)
        """.trimMargin()
        myFixture.configureByText("test.gd", code)
        myFixture.testInlays()
    }

    @Test
    fun testInlayHintsForBareSdkConstructorCall() {
        val code = """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = Vector2(<hint text="x:"/>1.0, <hint text="y:"/>2.0)
        """.trimMargin()
        myFixture.configureByText("test.gd", code)
        myFixture.testInlays()
    }
}
