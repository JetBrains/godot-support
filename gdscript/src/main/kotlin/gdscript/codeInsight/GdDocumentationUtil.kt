package gdscript.codeInsight

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol

object GdDocumentationUtil {

    fun createElementLink(reference: String, label: String): StringBuilder {
        val sb = StringBuilder()
        if (reference.isBlank() || label.isBlank()) {
            return sb
        }

        sb.append("&nbsp;&nbsp;<a href=\"" + DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL)
        sb.append(reference)
        sb.append("\">")
        sb.append(label)
        sb.append("</a>")
        sb.append("<br>")

        return sb
    }

}
