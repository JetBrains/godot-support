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
    fun testDocsUrlPlaceholderInUrlTag() {
        val docsUrlPlaceholder = "$" + "DOCS_URL"
        val parsed = GdGodotDocUtil.parseStyles(
            "[url=$docsUrlPlaceholder/tutorials/plugins/running_code_in_the_editor.html#instancing-scenes]Instancing scenes[/url]"
        )

        assertTrue(parsed.contains("https://docs.godotengine.org/en/stable/tutorials/plugins/running_code_in_the_editor.html#instancing-scenes"))
        assertTrue(parsed.contains("Instancing scenes"))
        assertFalse(parsed.contains(docsUrlPlaceholder))
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
    fun testReferenceLinksKeepKindAndQualifiedTarget() {
        val parsed = GdGodotDocUtil.parseStyles(
            "See [Node], [method Object.free], [member Node.owner], and [method _ready]."
        )

        assertTrue(parsed.contains("psi_element://Node"))
        assertTrue(parsed.contains("psi_element://method:Object.free"))
        assertTrue(parsed.contains("psi_element://member:Node.owner"))
        assertTrue(parsed.contains("psi_element://method:_ready"))
    }

    @Test
    fun testReferenceLinksKeepSpecialClassName() {
        val parsed = GdGodotDocUtil.parseStyles("See [method @GlobalScope.print].")

        assertTrue(parsed.contains("psi_element://method:@GlobalScope.print"))
        assertTrue(parsed.contains("@GlobalScope.print"))
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

    @Test
    fun testInlineCodeIsCodeNotItalic() {
        val parsed = GdGodotDocUtil.parseStyles("Returns [code]null[/code] on failure.")
        assertTrue(parsed.contains("<code>null</code>"))
        assertFalse(parsed.contains("<i>null</i>"))
    }

    @Test
    fun testCodeblockRendersAsPreformatted() {
        val parsed = GdGodotDocUtil.parseStyles(
            """
            Example:
            [codeblock]
            func _ready():
                print("hi")
            [/codeblock]
            """.trimIndent()
        )
        assertTrue(parsed.contains("<pre><code>"))
        assertTrue(parsed.contains("func _ready():"))
        assertTrue(parsed.contains("print(&quot;hi&quot;)") || parsed.contains("print(\"hi\")"))
        assertFalse(parsed.contains("[codeblock]"))
    }

    /**
     * Mirrors the `_validate_property` sample from `comment/object.gd`:
     * GDScript must stay preformatted, and brackets inside the code must not become type links.
     */
    @Test
    fun testCodeblocksStayPreformatted() {
        val source = """
            Override this method. See [method _get_property_list].
            [codeblocks]
            [gdscript]
            @tool
            extends Node

            @export var is_number_editable: bool:
            	set(value):
            		is_number_editable = value
            		notify_property_list_changed()
            @export var number: int

            func _validate_property(property: Dictionary):
            	if property.name == "number" and not is_number_editable:
            		property.usage |= PROPERTY_USAGE_READ_ONLY
            [/gdscript]
            [/codeblocks]
        """.trimIndent()

        val parsed = GdGodotDocUtil.parseStyles(source)
        val paragraph = GdDocUtil.paragraph(source).toString()

        assertTrue(parsed.contains("<pre><code>"))
        assertTrue(parsed.contains("<strong>GDScript</strong>"))
        assertTrue(parsed.contains("@tool"))
        assertTrue(parsed.contains("func _validate_property"))
        assertTrue(parsed.contains("set(value):"))

        // Prose references outside code still linkify.
        assertFalse(paragraph.contains("[method _get_property_list]"))
        assertTrue(paragraph.contains("_get_property_list"))
        assertTrue(paragraph.contains("<pre><code>"))
    }

    @Test
    fun testCodeblockPreservesRelativeIndent() {
        val parsed = GdGodotDocUtil.parseStyles(
            """
            [gdscript]
            func f():
                if true:
                    print(1)
            [/gdscript]
            """.trimIndent()
        )
        assertTrue(parsed.contains("<pre>"))
        // After common-indent strip, the if-body keeps relative indent.
        assertTrue(
            parsed.contains("if true:\n    print(1)") ||
                parsed.contains("if true:\n\tprint(1)") ||
                Regex("if true:\\s+print\\(1\\)").containsMatchIn(parsed.replace("&quot;", "\""))
        )
    }

    @Test
    fun testStripDocCommentPrefixKeepsIndent() {
        assertEquals(
            "\tset(value):",
            gdscript.psi.utils.GdCommentUtil.stripDocCommentPrefix("## \tset(value):")
        )
        assertEquals(
            "set(value):",
            gdscript.psi.utils.GdCommentUtil.stripDocCommentPrefix("## set(value):")
        )
    }

}
