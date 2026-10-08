package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.lang.documentation.QuickDocHighlightingHelper
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.openapi.util.text.StringUtil

/**
 * The renderer of the Godot BBCode of a `##` doc comment.
 * [renderToHtml] converts a style tag, a reference such as `[member x]`, a URL and a code block to HTML.
 * [GdDocHtml] lays the result out in the popup.
 */
object GdBBCodeRenderer {
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
    private val placeholderRegex = "$PLACEHOLDER_PREFIX(\\d+)$PLACEHOLDER_SUFFIX".toRegex()

    /** A tag with a fixed HTML form. [renderToHtml] replaces each pair in this order. */
    private val STYLE_TAGS: Map<String, String> = linkedMapOf(
        "[b]" to "<strong>",
        "[/b]" to "</strong>",
        "[i]" to "<a style=\"font-style: italic;\">",
        "[/i]" to "</a>",
        "[u]" to "<u>",
        "[/u]" to "</u>",
        "[s]" to "<s>",
        "[/s]" to "</s>",
        "[br]" to "<br>",
        "[center]" to "<div style=\"text-align: center;\">",
        "[/center]" to "</div>",
        "[kbd]" to "<code style=\"background: rgba(128,128,128,0.2); padding: 1px 4px; border-radius: 3px;\">",
        "[/kbd]" to "</code>",
    )

    /** A tag that escapes a literal bracket. A placeholder hides the entity from the reference rules. */
    private val BRACKET_ESCAPE_TAGS: Map<String, String> = linkedMapOf(
        "[lb]" to "&#91;",
        "[rb]" to "&#93;",
    )

    @NlsSafe
    fun expandDocsUrl(url: String): String = url.replace(DOCS_URL_PLACEHOLDER, DOCS_BASE_URL)

    /** Converts the Godot BBCode of [text] to the HTML of the documentation popup. */
    @NlsSafe
    fun renderToHtml(text: String, project: Project? = null): String {
        val protected = ArrayList<String>()
        var parsed = protectCodeRegions(text, project, protected)

        parsed = parsed.replaceAll(STYLE_TAGS)

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
            val link = GdDocHtml.typedElementLink(kind, referenced)
            link.toString()
        }

        // Try to replace unspecified references like [Object] or [Node]
        parsed = freeReference.replace(parsed) { match ->
            val full = match.value
            // Skip placeholders and already-produced HTML tags.
            if (full.contains(PLACEHOLDER_PREFIX) || full.startsWith("[/")) return@replace full
            val value = match.groupValues[1]
            if (value.contains('<') || value.contains('>')) return@replace full
            val link = GdDocHtml.elementLink(value)
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
            if (path.startsWith("res://")) placeholder(GdDocHtml.elementLink(path).toString(), protected) else match.value
        }
        result = resourceCodeTag.replace(result) { match ->
            val path = match.groupValues[1].trim()
            placeholder(GdDocHtml.elementLink(path).toString(), protected)
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
        for ((tag, entity) in BRACKET_ESCAPE_TAGS) {
            if (!result.contains(tag)) continue
            result = result.replace(tag, placeholder(entity, protected))
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

    /** Replaces every key of [replacements] with its value, in the order of the map. */
    private fun String.replaceAll(replacements: Map<String, String>): String =
        replacements.entries.fold(this) { text, (from, to) -> text.replace(from, to) }

    private fun placeholder(html: String, protected: MutableList<String>): String {
        val index = protected.size
        protected.add(html)
        return "$PLACEHOLDER_PREFIX$index$PLACEHOLDER_SUFFIX"
    }

    private fun restorePlaceholders(text: String, protected: List<String>): String {
        return placeholderRegex.replace(text) { match ->
            protected.getOrNull(match.groupValues[1].toInt()) ?: match.value
        }
    }

}
