package gdscript.codeInsight.documentation

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.openapi.util.text.HtmlChunk

object GdDocUtil {

    fun elementLink(reference: String, label: String): HtmlChunk {
        return HtmlChunk.link(DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL + reference, label)
    }

    fun packageLink(packageName: String, label: String): HtmlChunk {
        return elementLink("package:$packageName", label)
    }

}
