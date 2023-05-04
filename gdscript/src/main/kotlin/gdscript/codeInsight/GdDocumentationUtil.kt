package gdscript.codeInsight

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.lang.documentation.DocumentationMarkup

@Deprecated("remove")
object GdDocumentationUtil {

    private val dynamicReference = "\\[(member|constant|method|enum) (.+?)]".toRegex()
    private val links = "\\[link (.+?)](.+?)\\[/link]".toRegex()
    private val freeReference = "\\[([A-Z].+?)]".toRegex()

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

    @Deprecated("temporary public call")
    fun renderGodotDoc(lines: List<String>): String {
        return renderFullDoc(StringBuilder(), lines.toTypedArray()).toString()
    }

    private fun renderFullDoc(sb: StringBuilder, docLines: Array<String>): StringBuilder {
        var tutorials = false

        /*
        [member position]
        [method _local]
        [method Input.method]
        [enum Input.method]
        [constant NOTIFICATION_READY]
        [Object]

        [b]  styling
        [code]
        [i]

        [gdscript]  examples
        [csharp]
         */

        docLines.forEach {
            var line = it
                .replace("[b]", "<strong>")
                .replace("[/b]", "</strong>")
                .replace("[code]", "<i>")
                .replace("[/code]", "</i>")
                .replace("[codeblocks]", "")
                .replace("[/codeblocks]", "")

                .replace("[gdscript]", "${DocumentationMarkup.DEFINITION_START}<strong>GdScript</strong>")
                .replace("[csharp]", "${DocumentationMarkup.DEFINITION_START}<strong>C#</strong>")
                .replace("[/gdscript]", DocumentationMarkup.DEFINITION_END)
                .replace("[/csharp]", DocumentationMarkup.DEFINITION_END)

            // Links & tutorials
            val match = links.find(line)
            if (match != null) {
                if (!tutorials) {
                    tutorials = true
                    GdDocumentationUtil.paragraphHeader(sb, "Tutorials")
                }
                val link = GdDocumentationUtil.createLink(
                    match.groups[2]?.value ?: "",
                    match.groups[1]?.value ?: "",
                )
                sb.append(link)
                sb.append("<br />")
                return@forEach
            }

            // Replace specific references [member|constant|method _name]
            dynamicReference.findAll(line).forEach matched@{ match ->
                val value = match.groups[2]?.value ?: return@matched
                val link = GdDocumentationUtil.createElementLink(
                    value.split(".").last(),
                    value.replace("@", "_"),
                )
                line = line.replace(match.groups[0]?.value!!, link.toString())
            }

            // Try to replace unspecified references like [Object] or [Node]
            freeReference.findAll(line).forEach matched@{ match ->
                val value = match.groups[1]?.value?.replace("@", "_") ?: return@matched
                val link = GdDocumentationUtil.createElementLink(
                    value,
                    value,
                )
                line = line.replace(match.groups[0]?.value!!, link.toString())
            }

            sb.append(line)
            sb.append("<br />")
            sb.append("<br />")
        }

        return sb
    }

}
