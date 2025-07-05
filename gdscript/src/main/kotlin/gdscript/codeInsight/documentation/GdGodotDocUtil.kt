package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup

object GdGodotDocUtil {

    private val dynamicReference = "\\[(member|constant|method|enum) (.+?)]".toRegex()
    private val freeReference = "\\[(.+?)]".toRegex()
    private val DEFINITION_START = "</p>${DocumentationMarkup.DEFINITION_START}"
    private val DEFINITION_END = "${DocumentationMarkup.DEFINITION_END}<p style=\"padding: 5px 10px 0 10px;\">"

    fun parseStyles(text: String): String {
        var parsed = text.replace("[b]", "<strong>")
            .replace("[/b]", "</strong>")
            .replace("[i]", "<a style=\"font-style: italic;\">")
            .replace("[/i]", "</a>")
            .replace("[code]", "<i>")
            .replace("[/code]", "</i>")
            .replace("[codeblock]", DEFINITION_START)
            .replace("[/codeblock]", DEFINITION_END)
            .replace("[codeblocks]", DEFINITION_START)
            .replace("[/codeblocks]", DEFINITION_END)

            .replace("[gdscript]", "${DEFINITION_START}<strong>GdScript</strong>")
            .replace("[csharp]", "${DEFINITION_START}<strong>C#</strong>")
            .replace("[/gdscript]", DEFINITION_END)
            .replace("[/csharp]", DEFINITION_END)

        // Replace specific references [member|constant|method _name]
        dynamicReference.findAll(parsed).forEach matched@{ match ->
            val referenced = match.groups[2]?.value ?: return@matched
            val link = GdDocUtil.elementLink(
                referenced.split(".").last(),
                referenced.replace("@", "_"),
            )

            parsed = parsed.replace(match.groups[0]?.value!!, link.toString())
        }

        // Try to replace unspecified references like [Object] or [Node]
        freeReference.findAll(parsed).forEach matched@{ match ->
            val value = match.groups[1]?.value?.replace("@", "_") ?: return@matched
            val link = GdDocUtil.elementLink(value)
            parsed = parsed.replace(match.groups[0]?.value!!, link.toString())
        }

        return parsed
    }

}
