package gdscript.codeInsight.documentation

import com.intellij.openapi.util.text.HtmlChunk

object GdDocFragment {

    fun packagePath(sb: StringBuilder, packagePath: String) {
        val chunks = mutableListOf(
            HtmlChunk.tag("icon").attr("src", "AllIcons.Nodes.Package"),
            HtmlChunk.nbsp(),
        )

        var currentPath = ""
        packagePath.split("/").forEachIndexed { index, s ->
            currentPath += "/$s"
            if (index == 0) {
                currentPath = currentPath.trimStart('/')
            } else {
                chunks.add(HtmlChunk.text("/"))
            }

            chunks.add(GdDocUtil.packageLink(currentPath.trimStart('/'), s))
        }

        sb.appendLine(
            HtmlChunk.div()
                .setClass("bottom")
                .children(chunks)
        )
    }

}
