package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.lang.documentation.QuickDocHighlightingHelper
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.openapi.util.text.StringUtil

object GdGodotDocUtil {

    private val dynamicReference = "\\[(member|constant|method|enum|param|signal|theme_item) (.+?)]".toRegex()
    private val freeReference = "\\[(.+?)]".toRegex()
    private val colorTag = "\\[color=(.+?)](.*?)\\[/color]".toRegex()
    private val urlWithHrefTag = "\\[url=(.+?)](.*?)\\[/url]".toRegex()
    private val urlPlainTag = "\\[url](.*?)\\[/url]".toRegex()
    private val inlineCodeTag = "(?s)\\[code](.*?)\\[/code]".toRegex()
    private val codeBlocksTag = "(?s)\\[codeblocks](.*?)\\[/codeblocks]".toRegex()
    private val codeBlockTag = "(?s)\\[codeblock(?:\\s[^]]*)?](.*?)\\[/codeblock]".toRegex()
    private val languageBlockTag = "(?s)\\[gdscript](.*?)\\[/gdscript]".toRegex()
    private val csharpBlockTag = "(?s)\\[csharp].*?\\[/csharp]".toRegex()

    private val DEFINITION_START = "</p>${DocumentationMarkup.DEFINITION_START}"
    private val DEFINITION_END = "${DocumentationMarkup.DEFINITION_END}<p style=\"padding: 5px 10px 0 10px;\">"

    private const val PLACEHOLDER_PREFIX = "\uE000GDCODE"
    private const val PLACEHOLDER_SUFFIX = "\uE001"

    @JvmOverloads
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
            val full = match.groups[0]?.value ?: return@matched
            // Skip placeholders and already-produced HTML tags.
            if (full.contains(PLACEHOLDER_PREFIX) || full.startsWith("[/")) return@matched
            val value = match.groups[1]?.value?.replace("@", "_") ?: return@matched
            if (value.contains('<') || value.contains('>')) return@matched
            val link = GdDocUtil.elementLink(value)
            parsed = parsed.replace(full, link.toString())
        }

        return restorePlaceholders(parsed, protected)
    }

    private fun protectCodeRegions(text: String, project: Project?, protected: MutableList<String>): String {
        // Drop C# samples; keep GDScript and unlabeled code blocks.
        var result = csharpBlockTag.replace(text, "")

        result = codeBlocksTag.replace(result) { match ->
            placeholder(renderCodeBlocks(match.groupValues[1], project), protected)
        }
        result = codeBlockTag.replace(result) { match ->
            placeholder(renderPreBlock(match.groupValues[1], label = null, project = project, language = "gdscript"), protected)
        }
        // Orphan GDScript sections outside [codeblocks] (defensive).
        result = languageBlockTag.replace(result) { match ->
            placeholder(renderPreBlock(match.groupValues[1], "GDScript", project, "gdscript"), protected)
        }
        result = inlineCodeTag.replace(result) { match ->
            val code = match.groupValues[1]
            val rendered = if (project != null) {
                QuickDocHighlightingHelper.getStyledInlineCode(project, language = null, code = code)
            }
            else {
                "<code>${escapeCode(code)}</code>"
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
            "<pre><code>${escapeCode(normalized)}</code></pre>"
        }

        val header = if (label != null) "<strong>$label</strong>" else ""
        return "$DEFINITION_START$header$content$DEFINITION_END"
    }

    private fun normalizeCode(code: String): String {
        val lines = code.replace("\r\n", "\n").replace('\r', '\n').split('\n')
        // Drop a single leading/trailing empty line introduced by BBCode layout.
        val trimmedEnds = lines
            .dropWhile { it.isEmpty() }
            .dropLastWhile { it.isEmpty() }
        if (trimmedEnds.isEmpty()) return ""

        val minIndent = trimmedEnds
            .filter { it.isNotBlank() }
            .minOfOrNull { line -> line.indexOfFirst { !it.isWhitespace() }.let { if (it < 0) line.length else it } }
            ?: 0

        return trimmedEnds.joinToString("\n") { line ->
            if (line.isBlank()) ""
            else if (minIndent > 0 && line.length >= minIndent) line.substring(minIndent)
            else line
        }
    }

    private fun escapeCode(code: String): String = StringUtil.escapeXmlEntities(code)

    private fun placeholder(html: String, protected: MutableList<String>): String {
        val index = protected.size
        protected.add(html)
        return "$PLACEHOLDER_PREFIX$index$PLACEHOLDER_SUFFIX"
    }

    private fun restorePlaceholders(text: String, protected: List<String>): String {
        var result = text
        protected.indices.reversed().forEach { index ->
            result = result.replace("$PLACEHOLDER_PREFIX$index$PLACEHOLDER_SUFFIX", protected[index])
        }
        return result
    }

}
