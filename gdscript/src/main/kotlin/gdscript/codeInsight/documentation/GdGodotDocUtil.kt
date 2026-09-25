package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.text.HtmlChunk

object GdGodotDocUtil {

    private val dynamicReference = "\\[(member|constant|method|enum|param|signal|theme_item) (.+?)]".toRegex()
    private val freeReference = "\\[(.+?)]".toRegex()
    private val colorTag = "\\[color=(.+?)](.*?)\\[/color]".toRegex()
    private val urlWithHrefTag = "\\[url=(.+?)](.*?)\\[/url]".toRegex()
    private val urlPlainTag = "\\[url](.*?)\\[/url]".toRegex()
    private val DEFINITION_START = "</p>${DocumentationMarkup.DEFINITION_START}"
    private val DEFINITION_END = "${DocumentationMarkup.DEFINITION_END}<p style=\"padding: 5px 10px 0 10px;\">"

    @NlsSafe
    fun parseStyles(text: String): String {
        var parsed = text.replace("[b]", "<strong>")
            .replace("[/b]", "</strong>")
            .replace("[i]", "<a style=\"font-style: italic;\">")
            .replace("[/i]", "</a>")
            .replace("[u]", "<u>")
            .replace("[/u]", "</u>")
            .replace("[s]", "<s>")
            .replace("[/s]", "</s>")
            .replace("[center]", "<div style=\"text-align: center;\">")
            .replace("[/center]", "</div>")
            .replace("[kbd]", "<code style=\"background: rgba(128,128,128,0.2); padding: 1px 4px; border-radius: 3px;\">")
            .replace("[/kbd]", "</code>")
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

        // [color=X]...[/color]
        parsed = colorTag.replace(parsed) { match ->
            "<span style=\"color: ${match.groupValues[1]};\">${match.groupValues[2]}</span>"
        }

        // [url=href]label[/url] and [url]plain-url[/url]
        parsed = urlWithHrefTag.replace(parsed) { match ->
            HtmlChunk.link(match.groupValues[1], match.groupValues[2]).toString()
        }
        parsed = urlPlainTag.replace(parsed) { match ->
            HtmlChunk.link(match.groupValues[1], match.groupValues[1]).toString()
        }

        // Replace specific references [member|constant|method|enum|param|signal|theme_item _name]
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
