package gdscript.codeInsight.documentation

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.openapi.util.text.HtmlChunk

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

}
