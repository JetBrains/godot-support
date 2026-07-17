package com.jetbrains.godot.gdscript.annotator

import com.intellij.lang.annotation.HighlightSeverity
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.highlighter.GdHighlighterColors
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * `GdSetGetAnnotator.colorSetGet` used to be blind to SDK-inherited `get =`/`set =` accessors:
 * `element.resolveSymbolReference(METHOD)?.gdPsiSourceElement?.parent ?: return` bailed for any
 * SDK-backed method, so no static/instance semantic-highlighting color was ever applied for a
 * `get = <sdk-inherited-method>` accessor. Verifies the cast-free rewrite
 * (`symbol.hasModifier(STATIC)`, no PSI cast) fixes this for a real bundled SDK method
 * (`Node2D.apply_scale`, an instance method - see `sdk/4.5.0/Node2D.xml`), while a project-declared
 * static method keeps coloring correctly as before.
 */
@RunWith(JUnit4::class)
class GdSetGetAnnotatorTest : GdTestCaseWithSdk("highlighting") {

    // Multiple highlights can share the exact same range at a get=/set= accessor (e.g. this
    // annotator's own "method does not exist" ERROR co-exists with its silent color annotation, and
    // separately with PolySymbols' own generic semantic-highlighting pass) - filter specifically for
    // colorSetGet's own silent, description-less INFORMATION annotation, not the co-located ones.
    private fun colorAt(text: String, needle: String): com.intellij.openapi.editor.colors.TextAttributesKey? {
        myFixture.configureByText("Test.gd", text)
        val offset = text.lastIndexOf(needle)
        return myFixture.doHighlighting()
            .find {
                it.startOffset == offset && it.endOffset == offset + needle.length &&
                    it.severity == HighlightSeverity.INFORMATION && it.description == null
            }
            ?.forcedTextAttributesKey
    }

    @Test
    fun testSdkInheritedInstanceGetterColoredAsMethodCall() {
        val text = """
            |extends Node2D
            |
            |func warmup():
            |	apply_scale(Vector2.new())
            |
            |var x: int : get = apply_scale
        """.trimMargin()
        assertEquals(GdHighlighterColors.METHOD_CALL, colorAt(text, "apply_scale"))
    }

    @Test
    fun testProjectStaticGetterColoredAsStaticMethodCall() {
        val text = """
            |static func my_getter() -> int:
            |	return 1
            |
            |var x: int : get = my_getter
        """.trimMargin()
        assertEquals(GdHighlighterColors.STATIC_METHOD_CALL, colorAt(text, "my_getter"))
    }
}
