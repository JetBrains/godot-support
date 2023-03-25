package gdscript.codeInsight

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol

object GdDocumentationUtil {

    fun createLink(reference: String, label: String): StringBuilder {
        val sb = StringBuilder()
        if (reference.isBlank() || label.isBlank()) {
            return sb
        }

        sb.append("<a href=\"")
        sb.append(reference)
        sb.append("\">")
        sb.append(label)
        sb.append("</a>")

        return sb
    }

    fun createElementLink(reference: String, label: String): StringBuilder {
        return createLink(DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL + reference, label)
    }

    fun paragraphHeader(sb: StringBuilder, label: String) {
        sb.append("<strong>")
        sb.append(label)
        sb.append("</strong><br />")
    }

    fun propTable(sb: StringBuilder, lines: Iterable<Array<String>>) {
        sb.append("<table style=\"border-spacing: 0px;\">")
        lines.forEach { columns ->
            sb.append("<tr>")
            columns.forEachIndexed { index, value ->
                sb.append("<td style=\"")
                if (index == 0) sb.append("text-align: right; padding-right: 15px;")
                sb.append("\">")
                sb.append(createElementLink(value, value))
                sb.append("</td>")
            }
            sb.append("</tr>")
        }
        sb.append("</table>")
    }

    fun signalTable(sb: StringBuilder, lines: Iterable<Array<String>>) {
        sb.append("<table style=\"border-spacing: 0px;\">")
        lines.forEach { columns ->
            columns.forEachIndexed { index, value ->
                sb.append("<tr>")
                sb.append("<td>")
                if (index == 0) {
                    sb.append(value)
                } else {
                    sb.append(grayText(value))
                }
                sb.append("</td>")
                sb.append("</tr>")
            }
        }
        sb.append("</table>")
    }

    fun enumTable(sb: StringBuilder, lines: Iterable<Array<Pair<String, Array<Pair<String, String>>>>>) {
        lines.forEach { columns ->
            columns.forEach { enum ->
                sb.append(enum.first)
                sb.append(":<br />")
                sb.append("<ul>")
                enum.second.forEach { value ->
                    sb.append("<li>")
                    sb.append(value.first)
                    sb.append(grayText(" = ${value.second}"))
                    sb.append("</li>")
                }
                sb.append("</ul>")
            }
        }
    }

    fun grayText(text: String): String {
        return "<a style=\"color: gray;\">$text</a>"
    }

}
