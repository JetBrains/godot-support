package com.jetbrains.godot.gdscript.resolve

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * `GdSymbolResolverUtil.listConstructorSymbols` is the SDK-aware replacement for
 * `GdClassMemberUtil.listClassMemberDeclarations(..., constructors = true)`, which only ever saw a
 * project class's own PSI `_init` - never an SDK class's overloaded constructors. Verifies it reaches
 * real bundled SDK data using `Vector2`, which has 4 real constructor overloads (no-arg,
 * copy-from-`Vector2`, copy-from-`Vector2i`, `(x: float, y: float)` - see `sdk/4.5.0/Vector2.xml`).
 */
@RunWith(JUnit4::class)
class GdSymbolResolverUtilSdkTest : GdTestCaseWithSdk("highlighting") {

    @Test
    fun testListConstructorSymbolsReturnsAllVector2Overloads() {
        myFixture.configureByText("Test.gd", "func f():\n\tpass")
        val vector2 = GdSymbolResolverUtil.resolveCanonicalClassSymbol(project, "Vector2", myFixture.file)
        val ctors = GdSymbolResolverUtil.listConstructorSymbols(vector2)

        assertEquals(4, ctors.size)
        assertEquals(listOf(0, 1, 1, 2), ctors.mapNotNull { it.gdSignature?.parameters?.size }.sorted())
    }
}
