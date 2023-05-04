package gdscript.codeInsight.documentation

import ai.grazie.utils.capitalize
import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.util.text.HtmlChunk
import gdscript.psi.utils.GdCommentUtil

object GdDocUtil {

    fun elementLink(reference: String, label: String? = null): HtmlChunk {
        return HtmlChunk.link(DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL + reference, label ?: reference)
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
                HtmlChunk.raw("<tr>"),
                DocumentationMarkup.SECTION_HEADER_CELL.addText("${key.capitalize()}:"),
                DocumentationMarkup.SECTION_CONTENT_CELL.addText(lines.first()),
                HtmlChunk.raw("</tr>"),
                *lines.takeLast(lines.size - 1).map {
                    HtmlChunk.tag("tr").children(
                            DocumentationMarkup.SECTION_CONTENT_CELL,
                            DocumentationMarkup.SECTION_CONTENT_CELL.addText(it),
                    )
                }.toTypedArray(),
        )
    }

    fun paragraph(lines: List<String>): HtmlChunk {
        if (lines.isEmpty()) return HtmlChunk.empty()
        return HtmlChunk.p().style("padding: 5px 10px 0 10px;").children(
                *lines.map { HtmlChunk.text(" $it") }.toTypedArray()
        )
    }

}
