package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.lang.documentation.QuickDocHighlightingHelper
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.openapi.util.text.StringUtil

object GdGodotDocUtil {
    private const val DOCS_BASE_URL = "https://docs.godotengine.org/en/stable"
    private const val DOCS_URL_PLACEHOLDER = $$"$DOCS_URL"

    private val dynamicReference = "\\[(member|constant|method|enum|param|signal|theme_item|annotation) (.+?)]".toRegex()
    private val freeReference = "\\[(.+?)]".toRegex()
    private val colorTag = "\\[color=(.+?)](.*?)\\[/color]".toRegex()
    private val urlWithHrefTag = "\\[url=(.+?)](.*?)\\[/url]".toRegex()
    private val urlPlainTag = "\\[url](.*?)\\[/url]".toRegex()
    private val imgTag = "\\[img(?:\\s[^]]*)?](.*?)\\[/img]".toRegex()
    private val resourceCodeTag = "\\[code](res://[^\\[]*?)\\[/code]".toRegex()
    private val inlineCodeTag = "(?s)\\[code](.*?)\\[/code]".toRegex()
    private val codeBlocksTag = "(?s)\\[codeblocks](.*?)\\[/codeblocks]".toRegex()
    private val codeBlockTag = "(?s)\\[codeblock(?:\\s[^]]*)?](.*?)\\[/codeblock]".toRegex()
    private val languageBlockTag = "(?s)\\[gdscript](.*?)\\[/gdscript]".toRegex()

    private const val DEFINITION_START = "</p>${DocumentationMarkup.DEFINITION_START}"
    private const val DEFINITION_END = "${DocumentationMarkup.DEFINITION_END}<p style=\"padding: 5px 10px 0 10px;\">"

    private const val PLACEHOLDER_PREFIX = "\uE000GDCODE"
    private const val PLACEHOLDER_SUFFIX = "\uE001"

    @NlsSafe
    fun expandDocsUrl(url: String): String = url.replace(DOCS_URL_PLACEHOLDER, DOCS_BASE_URL)

    @NlsSafe
    fun parseStyles(text: String, project: Project? = null): String {
        val protected = ArrayList<String>()
        var parsed = protectCodeRegions(text, project, protected)

        parsed = parsed.replace("[b]", "<strong>")
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

        // [color=X]...[/color]
        parsed = colorTag.replace(parsed) { match ->
            "<span style=\"color: ${match.groupValues[1]};\">${match.groupValues[2]}</span>"
        }

        // [url=href]label[/url] and [url]plain-url[/url]
        parsed = urlWithHrefTag.replace(parsed) { match ->
            HtmlChunk.link(expandDocsUrl(match.groupValues[1]), match.groupValues[2]).toString()
        }
        parsed = urlPlainTag.replace(parsed) { match ->
            val url = expandDocsUrl(match.groupValues[1])
            HtmlChunk.link(url, url).toString()
        }

        // Replace specific references [member|constant|method|enum|param|signal|theme_item _name]
        parsed = dynamicReference.replace(parsed) { match ->
            val kind = match.groupValues[1]
            val referenced = match.groupValues[2]
            val link = GdDocUtil.typedElementLink(kind, referenced)
            link.toString()
        }

        // Try to replace unspecified references like [Object] or [Node]
        parsed = freeReference.replace(parsed) { match ->
            val full = match.value
            // Skip placeholders and already-produced HTML tags.
            if (full.contains(PLACEHOLDER_PREFIX) || full.startsWith("[/")) return@replace full
            val value = match.groupValues[1]
            if (value.contains('<') || value.contains('>')) return@replace full
            val link = GdDocUtil.elementLink(value)
            link.toString()
        }

        return restorePlaceholders(parsed, protected)
    }

    private fun protectCodeRegions(text: String, project: Project?, protected: MutableList<String>): String {
        var result = codeBlocksTag.replace(text) { match ->
            placeholder(renderCodeBlocks(match.groupValues[1], project), protected)
        }
        result = codeBlockTag.replace(result) { match ->
            placeholder(renderPreBlock(match.groupValues[1], label = null, project = project, language = "gdscript"), protected)
        }
        // Orphan GDScript sections outside [codeblocks] (defensive).
        result = languageBlockTag.replace(result) { match ->
            placeholder(renderPreBlock(match.groupValues[1], "GDScript", project, "gdscript"), protected)
        }
        // [img]res://path[/img] and [code]res://path[/code] become links to the resource.
        result = imgTag.replace(result) { match ->
            val path = match.groupValues[1].trim()
            if (path.startsWith("res://")) placeholder(GdDocUtil.elementLink(path).toString(), protected) else match.value
        }
        result = resourceCodeTag.replace(result) { match ->
            val path = match.groupValues[1].trim()
            placeholder(GdDocUtil.elementLink(path).toString(), protected)
        }
        result = inlineCodeTag.replace(result) { match ->
            val code = match.groupValues[1]
            val rendered = if (project != null) {
                QuickDocHighlightingHelper.getStyledInlineCode(project, language = null, code = code)
            }
            else {
                "<code>${StringUtil.escapeXmlEntities(code)}</code>"
            }
            placeholder(rendered, protected)
        }

        return result
    }

    private fun renderCodeBlocks(body: String, project: Project?): String {
        val sections = StringBuilder()
        languageBlockTag.findAll(body).forEach { match ->
            sections.append(renderPreBlock(match.groupValues[1], "GDScript", project, "gdscript"))
        }
        if (sections.isNotEmpty()) return sections.toString()
        // Plain body inside [codeblocks] without language tags.
        return renderPreBlock(body, label = null, project = project, language = null)
    }

    private fun renderPreBlock(code: String, label: String?, project: Project?, language: String?): String {
        val normalized = normalizeCode(code)
        val content = if (project != null) {
            QuickDocHighlightingHelper.getStyledCodeBlock(
                project,
                QuickDocHighlightingHelper.guessLanguage(language),
                normalized,
            )
        }
        else {
            "<pre><code>${StringUtil.escapeXmlEntities(normalized)}</code></pre>"
        }

        val header = if (label != null) "<strong>$label</strong>" else ""
        return "$DEFINITION_START$header$content$DEFINITION_END"
    }

    private fun normalizeCode(code: String): String {
        return code.replace("\r\n", "\n").trimIndent()
    }

    private fun placeholder(html: String, protected: MutableList<String>): String {
        val index = protected.size
        protected.add(html)
        return "$PLACEHOLDER_PREFIX$index$PLACEHOLDER_SUFFIX"
    }

    private fun restorePlaceholders(text: String, protected: List<String>): String {
        val placeholder = "\uE000GDCODE(\\d+)\uE001".toRegex()
        return placeholder.replace(text) { match ->
            protected.getOrNull(match.groupValues[1].toInt()) ?: match.value
        }
    }

}
