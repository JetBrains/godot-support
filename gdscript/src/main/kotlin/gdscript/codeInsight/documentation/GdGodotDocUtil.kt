package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup

object GdGodotDocUtil {

    private val dynamicReference = "\\[(member|constant|method|enum) (.+?)]".toRegex()
    private val freeReference = "\\[(.+?)]".toRegex()

    fun parserDescription(text: String): String {
        var parsed = text.replace("[b]", "<strong>")
            .replace("[/b]", "</strong>")
            .replace("[i]", "<a style=\"font-style: italic;\">")
            .replace("[/i]", "</a>")
            .replace("[code]", "<i>")
            .replace("[/code]", "</i>")
            .replace("[codeblocks]", "<code>")
            .replace("[/codeblocks]", "</code>")

            .replace("[gdscript]", "${DocumentationMarkup.DEFINITION_START}<strong>GdScript</strong>")
            .replace("[csharp]", "${DocumentationMarkup.DEFINITION_START}<strong>C#</strong>")
            .replace("[/gdscript]", DocumentationMarkup.DEFINITION_END)
            .replace("[/csharp]", DocumentationMarkup.DEFINITION_END)

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
