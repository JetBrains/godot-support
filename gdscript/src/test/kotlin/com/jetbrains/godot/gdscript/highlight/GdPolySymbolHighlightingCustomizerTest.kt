package com.jetbrains.godot.gdscript.highlight

import com.intellij.polySymbols.highlighting.PolySymbolHighlightingCustomizer
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.highlighter.GdHighlighterColors
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReference
import gdscript.psi.GdRefIdRef

class GdPolySymbolHighlightingCustomizerTest : BasePlatformTestCase() {

    fun testSymbolKindColoring() {
        val code = """
            |class_name HighlightTarget
            |
            |static var static_prop := 1
            |var instance_prop := 2
            |
            |static func static_method():
            |    pass
            |
            |func instance_method():
            |    pass
            |
            |class Inner:
            |    pass
            |
            |func use():
            |    static_method()
            |    instance_method()
            |    static_prop
            |    instance_prop
            |    Inner
        """.trimMargin()

        val file = myFixture.configureByText("HighlightTarget.gd", code)

        fun attributesOf(refText: String, occurrence: Int = 0): com.intellij.openapi.editor.colors.TextAttributesKey? {
            var seen = 0
            var offset = -1
            var idx = file.text.indexOf(refText)
            while (idx >= 0) {
                if (seen == occurrence) {
                    offset = idx
                    break
                }
                seen++
                idx = file.text.indexOf(refText, idx + 1)
            }
            check(offset >= 0) { "'$refText' occurrence #$occurrence not found" }
            val element = file.findElementAt(offset + 1)!!.parent as GdRefIdRef
            val symbol = element.resolveSymbolReference() ?: return null
            return PolySymbolHighlightingCustomizer.getSymbolTextAttributes(element, symbol, 0)
        }

        // Second occurrence of each identifier is the usage inside `use()`; the first is the declaration.
        assertEquals(GdHighlighterColors.STATIC_METHOD_CALL, attributesOf("static_method", 1))
        assertEquals(GdHighlighterColors.METHOD_CALL, attributesOf("instance_method", 1))
        assertEquals(GdHighlighterColors.MEMBER, attributesOf("static_prop", 1))
        assertEquals(GdHighlighterColors.MEMBER, attributesOf("instance_prop", 1))
        assertEquals(GdHighlighterColors.CLASS_TYPE, attributesOf("Inner", 1))
    }
}
