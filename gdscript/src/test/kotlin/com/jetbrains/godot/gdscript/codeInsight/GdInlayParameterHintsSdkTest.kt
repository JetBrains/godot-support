package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.openapi.application.EDT
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

/**
 * `GdInlayParameterHintProvider` used to be blind to SDK/engine callees: `symbol.gdPsiSourceElement
 * ?.parent ?: return` bailed for any SDK-backed method/constructor, so no inline parameter-name
 * hints ever showed for a call to an SDK/engine method or an SDK class's constructor. Verifies the
 * cast-free rewrite (dispatch on `symbol.kind` + `gdSignature`) fixes this, using the real bundled
 * `Vector2` SDK doc data. Each fixture starts with a harmless "warm-up" statement referencing the
 * same SDK class first - see `GdParamAnnotatorTest` for why (a cold-cache issue in the SDK class
 * symbol scope's own machinery, unrelated to this provider).
 */
@TestApplication
class GdInlayParameterHintsSdkTest {
    private val gd: GdSdkFixture by gdSdkFixture("codeInsight")

    @Test
    fun testInlayHintsForSdkMethodCall(): Unit = timeoutRunBlocking {
        val code = """
            |func f(v: Vector2):
            |	var warmup = Vector2.new()
            |	v.angle_to(<hint text="to:"/>warmup)
        """.trimMargin()
        gd.fixture.configureByText("test.gd", code)
        withContext(Dispatchers.EDT) {
            gd.fixture.testInlays()
        }
    }

    @Test
    fun testInlayHintsForSdkConstructorViaNew(): Unit = timeoutRunBlocking {
        val code = """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = Vector2.new(<hint text="x:"/>1.0, <hint text="y:"/>2.0)
        """.trimMargin()
        gd.fixture.configureByText("test.gd", code)
        withContext(Dispatchers.EDT) {
            gd.fixture.testInlays()
        }
    }

    @Test
    fun testInlayHintsForBareSdkConstructorCall(): Unit = timeoutRunBlocking {
        val code = """
            |func f():
            |	var warmup = Vector2.new()
            |	var v = Vector2(<hint text="x:"/>1.0, <hint text="y:"/>2.0)
        """.trimMargin()
        gd.fixture.configureByText("test.gd", code)
        withContext(Dispatchers.EDT) {
            gd.fixture.testInlays()
        }
    }
}
