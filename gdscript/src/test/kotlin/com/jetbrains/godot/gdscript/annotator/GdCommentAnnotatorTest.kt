package com.jetbrains.godot.gdscript.annotator

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.highlighter.GdHighlighterColors
import gdscript.utils.GdCustomRegionUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdCommentAnnotatorTest : BasePlatformTestCase() {

    private fun colorAt(text: String, needle: String): TextAttributesKey? {
        myFixture.configureByText("test.gd", text)
        val offset = text.indexOf(needle)
        if (offset < 0) return null
        return myFixture.doHighlighting()
            .find {
                it.startOffset == offset && it.endOffset == offset + needle.length &&
                    it.severity == HighlightSeverity.INFORMATION
            }
            ?.forcedTextAttributesKey
    }

    @Test
    fun testCustomRegionUtilMarkerLength() {
        assertEquals(7, GdCustomRegionUtil.getMarkerLength("#region"))
        assertEquals(7, GdCustomRegionUtil.getMarkerLength("#region Section"))
        assertEquals(7, GdCustomRegionUtil.getMarkerLength("#region\n"))
        assertEquals(10, GdCustomRegionUtil.getMarkerLength("#endregion"))
        assertEquals(10, GdCustomRegionUtil.getMarkerLength("#endregion Section"))
        assertEquals(0, GdCustomRegionUtil.getMarkerLength("# region"))
        assertEquals(0, GdCustomRegionUtil.getMarkerLength("#regional"))
        assertEquals(0, GdCustomRegionUtil.getMarkerLength("#region_1"))
        assertEquals(0, GdCustomRegionUtil.getMarkerLength("# comment"))
    }

    @Test
    fun testRegionKeywordHighlighted() {
        val text = "#region My Section\nvar x = 1\n#endregion\n"
        assertEquals(GdHighlighterColors.KEYWORD, colorAt(text, "#region"))
        assertEquals(GdHighlighterColors.KEYWORD, colorAt(text, "#endregion"))
    }

    @Test
    fun testRegionMarkerOnlyWithoutDescriptionHighlighted() {
        val text = "#region\nvar x = 1\n#endregion\n"
        assertEquals(GdHighlighterColors.KEYWORD, colorAt(text, "#region"))
        assertEquals(GdHighlighterColors.KEYWORD, colorAt(text, "#endregion"))
    }

    @Test
    fun testOrdinaryCommentNotHighlightedAsKeyword() {
        val text = "# normal comment\n"
        assertNull(colorAt(text, "# normal"))
    }

    @Test
    fun testRegionWithSpaceNotHighlightedAsKeyword() {
        val text = "# region My Section\n# endregion\n"
        assertNull(colorAt(text, "# region"))
        assertNull(colorAt(text, "# endregion"))
    }

    @Test
    fun testRegionalWordNotHighlightedAsKeyword() {
        val text = "#regional news\n"
        assertNull(colorAt(text, "#region"))
    }

    @Test
    fun testDocCommentHighlighted() {
        val text = "## Documented class\n"
        assertEquals(GdHighlighterColors.DOC_COMMENT, colorAt(text, "## Documented class"))
    }
}
