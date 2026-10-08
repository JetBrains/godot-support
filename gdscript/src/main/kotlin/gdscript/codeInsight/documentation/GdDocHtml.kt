package gdscript.codeInsight.documentation

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.text.HtmlChunk
import gdscript.psi.types.GdDocumented
import java.util.Locale.getDefault

/**
 * The HTML vocabulary of the quick documentation popup.
 * It builds a link, a table and a paragraph with [DocumentationMarkup].
 * It knows no Godot markup. [GdBBCodeRenderer] converts that markup first.
 */
object GdDocHtml {

    private val CONTAINER_TYPE_REGEX = "(Array|Dictionary)\\[(.*?)]?".toRegex()

    /**
     * A link to the declaration of [reference], with [label] as the text.
     * A container type, such as `Array[int]`, links to its element type, because the container has no declaration.
     */
    fun elementLink(reference: String, @NlsSafe label: String? = null): HtmlChunk {
        val target = DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL + linkTarget(reference)
        return HtmlChunk.link(target, label ?: reference)
    }

    /**
     * The declaration that a type name points to.
     * It unwraps every level of `Array[...]`.
     * It keeps the container name when the type has two parameters, such as `Dictionary[String, int]`,
     * or when the type is malformed, such as `Array[`.
     */
    private fun linkTarget(reference: String): String {
        var result = reference
        while (true) {
            val match = CONTAINER_TYPE_REGEX.matchEntire(result) ?: return result
            val element = match.groupValues[2].trim()
            if (element.isEmpty() || element.contains(',')) return match.groupValues[1]
            result = element
        }
    }

    fun typedElementLink(kind: String, reference: String, @NlsSafe label: String? = null): HtmlChunk {
        return elementLink("$kind:$reference", label ?: reference)
    }

    fun iconed(icon: String): MutableList<HtmlChunk> {
        return mutableListOf(
            HtmlChunk.tag("icon").attr("src", icon),
            HtmlChunk.nbsp(),
        )
    }

    fun listTable(key: String, lines: List<HtmlChunk>): HtmlChunk {
        if (lines.isEmpty()) return HtmlChunk.empty()
        return DocumentationMarkup.SECTIONS_TABLE.children(
            tableHeader(key, lines.first()),
            *lines.drop(1).map { tableLine(it) }.toTypedArray(),
        )
    }

    fun descriptionListTable(key: String, items: List<Pair<HtmlChunk, HtmlChunk>>): HtmlChunk {
        if (items.isEmpty()) return HtmlChunk.empty()
        return DocumentationMarkup.SECTIONS_TABLE.children(
            tableTitle(key).wrapWith("tr"),
            HtmlChunk.tag("tr").child(
                HtmlChunk.tag("td").child(
                    HtmlChunk.ul().style("margin-top: 0;").children(
                        *items.map {
                            HtmlChunk.li().style("margin-bottom: 10px;").children(
                                it.first,
                                HtmlChunk.br(),
                                it.second,
                            )
                        }.toTypedArray()
                    )
                )
            )
        )
    }

    fun descriptionListsTable(key: String, items: List<Pair<HtmlChunk, List<HtmlChunk>>>): HtmlChunk {
        if (items.isEmpty()) return HtmlChunk.empty()
        return DocumentationMarkup.SECTIONS_TABLE.children(
            tableTitle(key).wrapWith("tr"),
            *items.map {
                HtmlChunk.fragment(
                    HtmlChunk.tag("td").style("padding-left: 10px;").child(it.first).wrapWith("tr"),
                    HtmlChunk.tag("tr").child(
                        HtmlChunk.tag("td").style("padding-left: 25px;").child(
                            HtmlChunk.ul().style("margin: 0;").children(
                                *it.second.map { value ->
                                    HtmlChunk.li().child(value)
                                }.toTypedArray()
                            )
                        )
                    ),
                )
            }.toTypedArray(),
        )
    }

    fun propertyTable(key: String, lines: List<Pair<HtmlChunk, HtmlChunk>>): HtmlChunk {
        if (lines.isEmpty()) return HtmlChunk.empty()

        return DocumentationMarkup.SECTIONS_TABLE.children(
            HtmlChunk.tag("tr").children(
                tableTitle(key),
                propertyTableLine(lines.first()),
            ),
            *lines.drop(1).map {
                HtmlChunk.tag("tr").child(propertyTableLine(it, true))
            }.toTypedArray(),
        )
    }

    fun paragraph(description: String, project: Project? = null): HtmlChunk {
        // Parse the full description first so multi-line [codeblock]/[codeblocks] stay one unit
        // and brackets inside code samples are not treated as type links.
        val parsed = GdBBCodeRenderer.renderToHtml(description, project)
        // Keep <pre> bodies on one logical line so per-line <br> insertion cannot break them.
        val lines = maskNewlinesInsidePre(parsed).split("\n")
        val blocks = buildList<HtmlChunk> {
            var i = 0
            while (i < lines.size) {
                if (isBulletLine(lines[i])) {
                    val bullets = lines.subList(i, lines.size).takeWhile(::isBulletLine)
                    add(HtmlChunk.ul().children(bullets.map { HtmlChunk.li().addRaw(unmaskNewlines(bulletContent(it))) }))
                    i += bullets.size
                } else {
                    val restored = unmaskNewlines(lines[i])
                    val skipBreak = isStructuralBlockLine(restored) ||
                        (i + 1 < lines.size && isStructuralBlockLine(unmaskNewlines(lines[i + 1])))
                    add(HtmlChunk.fragment(HtmlChunk.raw(restored), if (skipBreak) HtmlChunk.empty() else HtmlChunk.br()))
                    i++
                }
            }
        }

        return HtmlChunk.p().style("padding: 5px 10px 0 10px;").children(blocks)
    }

    private const val PRE_NEWLINE_MASK = '\uE002'

    private fun maskNewlinesInsidePre(html: String): String {
        return PRE_BLOCK_REGEX.replace(html) { match ->
            match.value.replace('\n', PRE_NEWLINE_MASK)
        }
    }

    private fun unmaskNewlines(text: String): String = text.replace(PRE_NEWLINE_MASK, '\n')

    private val PRE_BLOCK_REGEX = "(?s)<pre\\b[^>]*>.*?</pre>".toRegex()

    private fun isStructuralBlockLine(line: String): Boolean {
        val trimmed = line.trim()
        return trimmed.contains("<pre") ||
            trimmed.contains("</pre>") ||
            trimmed.contains(DocumentationMarkup.DEFINITION_START) ||
            trimmed.contains(DocumentationMarkup.DEFINITION_END)
    }

    /** A `-` or `*` prefixed bullet-list line, per the Godot doc comment BBCode subset. */
    private fun isBulletLine(line: String): Boolean {
        val trimmed = line.trimStart()
        return trimmed.startsWith("- ") || trimmed.startsWith("* ")
    }

    /** The text of a bullet line, without its own marker. A second marker belongs to the text. */
    private fun bulletContent(line: String): String {
        val trimmed = line.trimStart()
        return if (trimmed.startsWith("- ")) trimmed.substring(2) else trimmed.removePrefix("* ")
    }

    fun appendDescription(element: GdDocumented): HtmlChunk {
        return appendDescription(element.description())
    }

    fun appendDescription(description: String?): HtmlChunk {
        if (description.isNullOrBlank()) return HtmlChunk.empty()
        return HtmlChunk.fragment(
            HtmlChunk.br(),
            DocumentationMarkup.GRAYED_ELEMENT.addRaw(GdBBCodeRenderer.renderToHtml(description)),
        )
    }

    private fun tableHeader(header: String, item: HtmlChunk): HtmlChunk {
        return tableHeader(header, listOf(item))
    }

    private fun tableHeader(header: String, items: List<HtmlChunk>): HtmlChunk {
        return HtmlChunk.tag("tr").children(
            tableTitle(header),
            *items.map { DocumentationMarkup.SECTION_CONTENT_CELL.child(it) }.toTypedArray(),
        )
    }

    private fun tableLine(item: HtmlChunk): HtmlChunk {
        return tableLine(listOf(item))
    }

    private fun tableLine(items: List<HtmlChunk>): HtmlChunk {
        return HtmlChunk.tag("tr").children(
            DocumentationMarkup.SECTION_CONTENT_CELL,
            *items.map { DocumentationMarkup.SECTION_CONTENT_CELL.child(it) }.toTypedArray(),
        )
    }

    private fun tableTitle(title: String): HtmlChunk {
        @NlsSafe val text = "${title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }}:"
        return DocumentationMarkup.SECTION_HEADER_CELL.addText(text)
    }

    private fun propertyTableLine(value: Pair<HtmlChunk, HtmlChunk>, withEmptyCell: Boolean = false): HtmlChunk {
        return HtmlChunk.fragment(
            if (withEmptyCell) HtmlChunk.tag("td") else HtmlChunk.empty(),
            DocumentationMarkup.SECTION_CONTENT_CELL.attr("align", "right").style("padding-right: 10px;")
                .child(value.first),
            DocumentationMarkup.SECTION_CONTENT_CELL.child(value.second),
        )
    }

}
