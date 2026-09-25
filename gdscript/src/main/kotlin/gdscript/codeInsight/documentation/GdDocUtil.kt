package gdscript.codeInsight.documentation

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.HtmlChunk
import gdscript.codeInsight.GdDocumentationProvider
import gdscript.psi.types.GdDocumented
import java.util.Locale
import java.util.Locale.getDefault

object GdDocUtil {

    fun packageLink(reference: String, @NlsSafe label: String? = null): HtmlChunk {
        return elementLink("${GdDocumentationProvider.LINK_PACKAGE}:${reference.trimStart('/')}", label)
    }

    fun elementLink(reference: String, @NlsSafe label: String? = null): HtmlChunk {
        var parsedReference = reference
        if (reference.startsWith("Array[")) parsedReference = reference.substring(6, reference.length - 1)

        return HtmlChunk.link(DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL + parsedReference, label ?: reference)
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
                *lines.takeLast(lines.size - 1).map { tableLine(it) }.toTypedArray(),
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
                *lines.takeLast(lines.size - 1).map {
                    HtmlChunk.tag("tr").child(propertyTableLine(it, true))
                }.toTypedArray(),
        )
    }

    fun paragraph(description: String): HtmlChunk {
        val lines = description.split("\n")
        if (lines.isEmpty()) return HtmlChunk.empty()

        val blocks = mutableListOf<HtmlChunk>()
        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            if (isBulletLine(line)) {
                val items = mutableListOf<HtmlChunk>()
                while (i < lines.size && isBulletLine(lines[i])) {
                    items.add(HtmlChunk.tag("li").addRaw(GdGodotDocUtil.parseStyles(bulletContent(lines[i]))))
                    i++
                }
                blocks.add(HtmlChunk.tag("ul").children(*items.toTypedArray()))
            } else {
                blocks.add(HtmlChunk.fragment(
                        HtmlChunk.raw(GdGodotDocUtil.parseStyles(line)),
                        if (!line.endsWith("<pre>") && !line.startsWith("</pre>")) HtmlChunk.br() else HtmlChunk.empty(),
                ))
                i++
            }
        }

        return HtmlChunk.p().style("padding: 5px 10px 0 10px;").children(*blocks.toTypedArray())
    }

    /** A `-` or `*` prefixed bullet-list line, per the Godot doc comment BBCode subset. */
    private fun isBulletLine(line: String): Boolean {
        val trimmed = line.trimStart()
        return trimmed.startsWith("- ") || trimmed.startsWith("* ")
    }

    private fun bulletContent(line: String): String {
        val trimmed = line.trimStart()
        return trimmed.removePrefix("- ").removePrefix("* ")
    }

    fun appendDescription(element: GdDocumented): HtmlChunk {
        return appendDescription(element.description())
    }

    fun appendDescription(description: String?): HtmlChunk {
        if (description.isNullOrBlank()) return HtmlChunk.empty()
        return HtmlChunk.fragment(
                HtmlChunk.br(),
                DocumentationMarkup.GRAYED_ELEMENT.addRaw(GdGodotDocUtil.parseStyles(description)),
        )
    }

    /**
     * ------------------------------- HELPERS -------------------------------
     */

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
