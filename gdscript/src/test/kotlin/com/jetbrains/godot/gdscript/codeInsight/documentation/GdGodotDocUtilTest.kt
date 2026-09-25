package com.jetbrains.godot.gdscript.codeInsight.documentation

import gdscript.codeInsight.documentation.GdDocUtil
import gdscript.codeInsight.documentation.GdGodotDocUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** Covers the Godot BBCode subset supported by [GdGodotDocUtil.parseStyles]. */
@RunWith(JUnit4::class)
class GdGodotDocUtilTest {

    @Test
    fun testUnderline() {
        val parsed = GdGodotDocUtil.parseStyles("[u]underlined[/u]")
        assertEquals("<u>underlined</u>", parsed)
    }

    @Test
    fun testStrikethrough() {
        val parsed = GdGodotDocUtil.parseStyles("[s]struck[/s]")
        assertEquals("<s>struck</s>", parsed)
    }

    @Test
    fun testColor() {
        val parsed = GdGodotDocUtil.parseStyles("[color=red]warning[/color]")
        assertTrue(parsed.contains("color: red;"))
        assertTrue(parsed.contains("warning"))
    }

    @Test
    fun testUrlWithHref() {
        val parsed = GdGodotDocUtil.parseStyles("[url=https://godotengine.org]Godot[/url]")
        assertTrue(parsed.contains("https://godotengine.org"))
        assertTrue(parsed.contains("Godot"))
    }

    @Test
    fun testUrlPlain() {
        val parsed = GdGodotDocUtil.parseStyles("[url]https://godotengine.org[/url]")
        assertTrue(parsed.contains("https://godotengine.org"))
    }

    @Test
    fun testParamReference() {
        val parsed = GdGodotDocUtil.parseStyles("See [param value].")
        assertTrue(parsed.contains("value"))
        assertFalse(parsed.contains("[param"))
    }

    @Test
    fun testSignalReference() {
        val parsed = GdGodotDocUtil.parseStyles("Emits [signal changed].")
        assertTrue(parsed.contains("changed"))
        assertFalse(parsed.contains("[signal"))
    }

    @Test
    fun testThemeItemReference() {
        val parsed = GdGodotDocUtil.parseStyles("Uses [theme_item font_color].")
        assertTrue(parsed.contains("font_color"))
        assertFalse(parsed.contains("[theme_item"))
    }

    /** Realistic sample, close to the `global_rotation` doc comment quoted in the ticket. */
    @Test
    fun testTicketSample() {
        val parsed = GdGodotDocUtil.parseStyles(
            "Global rotation, see [url=https://docs.godotengine.org]docs[/url]. " +
                "[b]Note:[/b] the value returned is [member rotation]."
        )
        assertTrue(parsed.contains("<strong>Note:</strong>"))
        assertTrue(parsed.contains("docs.godotengine.org"))
        assertFalse(parsed.contains("[member"))
    }

    @Test
    fun testBulletList() {
        val parsed = GdDocUtil.paragraph("- first item\n- second item").toString()
        assertTrue(parsed.contains("<ul>"))
        assertTrue(parsed.contains("<li>first item</li>"))
        assertTrue(parsed.contains("<li>second item</li>"))
    }

}
