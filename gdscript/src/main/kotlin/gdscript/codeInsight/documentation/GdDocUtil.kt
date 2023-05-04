package gdscript.codeInsight.documentation

import ai.grazie.utils.capitalize
import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.util.text.HtmlChunk

object GdDocUtil {

    fun elementLink(reference: String, label: String? = null): HtmlChunk {
        var parsedReference = reference
        if (reference.startsWith("Array[")) parsedReference = reference.substring(6, reference.length - 1)

        return HtmlChunk.link(DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL + parsedReference, label ?: reference)
    }

    fun packageLink(packageName: String, label: String): HtmlChunk {
        return elementLink("package:$packageName", label)
    }

    fun iconed(icon: String): MutableList<HtmlChunk> {
        return mutableListOf(
            HtmlChunk.tag("icon").attr("src", icon),
            HtmlChunk.nbsp(),
        )
    }

    fun listTable(key: String, lines: List<String>): HtmlChunk {
        if (lines.isEmpty()) return HtmlChunk.empty()
        return DocumentationMarkup.SECTIONS_TABLE.children(
            tableHeader(key, lines.first()),
            *lines.takeLast(lines.size - 1).map { tableLine(it) }.toTypedArray(),
        )
    }

    fun descriptionListTable(key: String, items: List<Pair<String, String>>): HtmlChunk {
        if (items.isEmpty()) return HtmlChunk.empty()
        return DocumentationMarkup.SECTIONS_TABLE.children(
            tableTitle(key).wrapWith("tr"),
            *items.map {
                HtmlChunk.fragment(
                    HtmlChunk.tag("tr").children(
                        DocumentationMarkup.SECTION_CONTENT_CELL,
                        DocumentationMarkup.SECTION_CONTENT_CELL.addRaw(it.first),
                    ),
                    HtmlChunk.tag("tr").children(
                        DocumentationMarkup.SECTION_CONTENT_CELL,
                        DocumentationMarkup.SECTION_CONTENT_CELL.addRaw(it.second),
                    ),
                )
            }.toTypedArray()
        )
    }

    fun propertyTable(key: String, lines: List<Pair<String, String>>): HtmlChunk {
        if (lines.isEmpty()) return HtmlChunk.empty()

        return DocumentationMarkup.SECTIONS_TABLE.children(
            HtmlChunk.raw("<tr>"),
            tableTitle(key),
            propertyTableLine(lines.first()),
            HtmlChunk.raw("</tr>"),
            *lines.takeLast(lines.size - 1).map {
                HtmlChunk.tag("tr").child(propertyTableLine(it, true))
            }.toTypedArray(),
        )
    }

    fun paragraph(lines: List<String>): HtmlChunk {
        if (lines.isEmpty()) return HtmlChunk.empty()
        return HtmlChunk.p().style("padding: 5px 10px 0 10px;").children(
            *lines.map {
                HtmlChunk.fragment(
                    HtmlChunk.raw(it),
                    if (!it.endsWith("<pre>") && !it.startsWith("</pre>")) HtmlChunk.br() else HtmlChunk.empty(),
                )
            }.toTypedArray()
        )
    }

    /**
     * ------------------------------- HELPERS -------------------------------
     */

    private fun tableHeader(header: String, item: String): HtmlChunk {
        return tableHeader(header, listOf(item))
    }

    private fun tableHeader(header: String, items: List<String>): HtmlChunk {
        return HtmlChunk.tag("tr").children(
            tableTitle(header),
            *items.map { DocumentationMarkup.SECTION_CONTENT_CELL.addRaw(it) }.toTypedArray(),
        )
    }

    private fun tableLine(item: String): HtmlChunk {
        return tableLine(listOf(item))
    }

    private fun tableLine(items: List<String>): HtmlChunk {
        return HtmlChunk.tag("tr").children(
            DocumentationMarkup.SECTION_CONTENT_CELL,
            *items.map { DocumentationMarkup.SECTION_CONTENT_CELL.addRaw(it) }.toTypedArray(),
        )
    }

    private fun tableTitle(title: String): HtmlChunk {
        return DocumentationMarkup.SECTION_HEADER_CELL.addText("${title.capitalize()}:")
    }

    private fun propertyTableLine(value: Pair<String, String>, withEmptyCell: Boolean = false): HtmlChunk {
        return HtmlChunk.fragment(
            if (withEmptyCell) HtmlChunk.tag("td") else HtmlChunk.empty(),
            DocumentationMarkup.SECTION_CONTENT_CELL.attr("align", "right").style("padding-right: 10px;")
                .child(elementLink(value.first)),
            DocumentationMarkup.SECTION_CONTENT_CELL.child(elementLink(value.second)),
        )
    }

}
