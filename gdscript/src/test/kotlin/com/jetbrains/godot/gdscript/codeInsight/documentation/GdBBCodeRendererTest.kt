package com.jetbrains.godot.gdscript.codeInsight.documentation

import com.intellij.openapi.util.text.HtmlChunk
import gdscript.codeInsight.documentation.GdDocHtml
import gdscript.codeInsight.documentation.GdBBCodeRenderer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** Covers the Godot BBCode subset supported by [GdBBCodeRenderer.renderToHtml]. */
@RunWith(JUnit4::class)
class GdBBCodeRendererTest {

    @Test
    fun testUnderline() {
        val parsed = GdBBCodeRenderer.renderToHtml("[u]underlined[/u]")
        assertEquals("<u>underlined</u>", parsed)
    }

    @Test
    fun testStrikethrough() {
        val parsed = GdBBCodeRenderer.renderToHtml("[s]struck[/s]")
        assertEquals("<s>struck</s>", parsed)
    }

    @Test
    fun testColor() {
        val parsed = GdBBCodeRenderer.renderToHtml("[color=red]warning[/color]")
        assertTrue(parsed.contains("color: red;"))
        assertTrue(parsed.contains("warning"))
    }

    @Test
    fun testUrlWithHref() {
        val parsed = GdBBCodeRenderer.renderToHtml("[url=https://godotengine.org]Godot[/url]")
        assertTrue(parsed.contains("https://godotengine.org"))
        assertTrue(parsed.contains("Godot"))
    }

    @Test
    fun testUrlPlain() {
        val parsed = GdBBCodeRenderer.renderToHtml("[url]https://godotengine.org[/url]")
        assertTrue(parsed.contains("https://godotengine.org"))
    }

    @Test
    fun testDocsUrlPlaceholderInUrlTag() {
        val docsUrlPlaceholder = "$" + "DOCS_URL"
        val parsed = GdBBCodeRenderer.renderToHtml(
            "[url=$docsUrlPlaceholder/tutorials/plugins/running_code_in_the_editor.html#instancing-scenes]Instancing scenes[/url]"
        )

        assertTrue(parsed.contains("https://docs.godotengine.org/en/stable/tutorials/plugins/running_code_in_the_editor.html#instancing-scenes"))
        assertTrue(parsed.contains("Instancing scenes"))
        assertFalse(parsed.contains(docsUrlPlaceholder))
    }

    @Test
    fun testParamReference() {
        val parsed = GdBBCodeRenderer.renderToHtml("See [param value].")
        assertTrue(parsed.contains("value"))
        assertFalse(parsed.contains("[param"))
    }

    @Test
    fun testSignalReference() {
        val parsed = GdBBCodeRenderer.renderToHtml("Emits [signal changed].")
        assertTrue(parsed.contains("changed"))
        assertFalse(parsed.contains("[signal"))
    }

    @Test
    fun testReferenceLinksKeepKindAndQualifiedTarget() {
        val parsed = GdBBCodeRenderer.renderToHtml(
            "See [Node], [method Object.free], [member Node.owner], and [method _ready]."
        )

        assertTrue(parsed.contains("psi_element://Node"))
        assertTrue(parsed.contains("psi_element://method:Object.free"))
        assertTrue(parsed.contains("psi_element://member:Node.owner"))
        assertTrue(parsed.contains("psi_element://method:_ready"))
    }

    @Test
    fun testReferenceLinksKeepSpecialClassName() {
        val parsed = GdBBCodeRenderer.renderToHtml("See [method @GlobalScope.print].")

        assertTrue(parsed.contains("psi_element://method:@GlobalScope.print"))
        assertTrue(parsed.contains("@GlobalScope.print"))
    }

    @Test
    fun testThemeItemReference() {
        val parsed = GdBBCodeRenderer.renderToHtml("Uses [theme_item font_color].")
        assertTrue(parsed.contains("font_color"))
        assertFalse(parsed.contains("[theme_item"))
    }

    /** Realistic sample, close to the `global_rotation` doc comment quoted in the ticket. */
    @Test
    fun testTicketSample() {
        val parsed = GdBBCodeRenderer.renderToHtml(
            "Global rotation, see [url=https://docs.godotengine.org]docs[/url]. " +
                "[b]Note:[/b] the value returned is [member rotation]."
        )
        assertTrue(parsed.contains("<strong>Note:</strong>"))
        assertTrue(parsed.contains("docs.godotengine.org"))
        assertFalse(parsed.contains("[member"))
    }

    @Test
    fun testBulletList() {
        val parsed = GdDocHtml.paragraph("- first item\n- second item").toString()
        assertTrue(parsed.contains("<ul>"))
        assertTrue(parsed.contains("<li>first item</li>"))
        assertTrue(parsed.contains("<li>second item</li>"))
    }

    /** A second marker belongs to the text of the bullet, so only the first one goes away. */
    @Test
    fun testBulletListStripsOneMarkerOnly() {
        val parsed = GdDocHtml.paragraph("- * double marker\n* - double marker").toString()
        assertTrue(parsed, parsed.contains("<li>* double marker</li>"))
        assertTrue(parsed, parsed.contains("<li>- double marker</li>"))
    }

    /**
     * A container type links to its element type.
     * A malformed type, such as `Array[`, must not throw, and a `Dictionary` links to the container.
     */
    @Test
    fun testContainerTypeLinks() {
        assertEquals("psi_element://int", href(GdDocHtml.elementLink("Array[int]")))
        assertEquals("psi_element://int", href(GdDocHtml.elementLink("Array[Array[int]]")))
        assertEquals("psi_element://Array", href(GdDocHtml.elementLink("Array[]")))
        assertEquals("psi_element://Array", href(GdDocHtml.elementLink("Array[")))
        assertEquals("psi_element://int", href(GdDocHtml.elementLink("Array[int")))
        assertEquals("psi_element://Dictionary", href(GdDocHtml.elementLink("Dictionary[String, int]")))
        assertEquals("psi_element://Node", href(GdDocHtml.elementLink("Node")))
    }

    /** The reported line that broke the rendering with an exception. */
    @Test
    fun testMalformedContainerTypesInProse() {
        val parsed = GdBBCodeRenderer.renderToHtml(
            "Bad types: [Array[], [Array[int], [Array[Array[int]]], [Dictionary[String, int]]."
        )

        assertTrue(parsed, parsed.contains("psi_element://Array\""))
        assertTrue(parsed, parsed.contains("psi_element://int\""))
        assertTrue(parsed, parsed.contains("psi_element://Dictionary\""))
    }

    /** `[br]` is a line break. The free-reference rule must not turn it into a link. */
    @Test
    fun testLineBreakTag() {
        val parsed = GdBBCodeRenderer.renderToHtml("First line.[br]Second line.[br][br]Third line.")

        assertEquals("First line.<br>Second line.<br><br>Third line.", parsed)
    }

    /** `[lb]` and `[rb]` are the escapes of a literal bracket. */
    @Test
    fun testBracketEscapeTags() {
        val parsed = GdBBCodeRenderer.renderToHtml("Use [lb]0[rb] for the first item.")

        assertEquals("Use &#91;0&#93; for the first item.", parsed)
    }

    private fun href(link: HtmlChunk): String {
        val html = link.toString()
        return Regex("href=\"([^\"]*)\"").find(html)!!.groupValues[1]
    }

    @Test
    fun testInlineCodeIsCodeNotItalic() {
        val parsed = GdBBCodeRenderer.renderToHtml("Returns [code]null[/code] on failure.")
        assertTrue(parsed.contains("<code>null</code>"))
        assertFalse(parsed.contains("<i>null</i>"))
    }

    @Test
    fun testCodeblockRendersAsPreformatted() {
        val parsed = GdBBCodeRenderer.renderToHtml(
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

        val parsed = GdBBCodeRenderer.renderToHtml(source)
        val paragraph = GdDocHtml.paragraph(source).toString()

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
        val parsed = GdBBCodeRenderer.renderToHtml(
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
