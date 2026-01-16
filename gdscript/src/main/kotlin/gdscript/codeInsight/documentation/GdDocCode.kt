package gdscript.codeInsight.documentation

import com.intellij.ide.highlighter.HighlighterFactory
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.ui.ColorUtil
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.psi.GdTypes

object GdDocCode {

    fun createHighlightedSnippet(code: String, project: Project): List<HtmlChunk> {
        val scheme = EditorColorsManager.getInstance().globalScheme
        val psi = PsiFileFactory.getInstance(project).createFileFromText(GdLanguage, code)
        val highlighter = HighlighterFactory.createHighlighter(psi.virtualFile, scheme, project)

        return iterate(code, highlighter)
    }

    private fun iterate(code: String, highlighter: EditorHighlighter): List<HtmlChunk> {
        highlighter.setText(code)
        val iterator = highlighter.createIterator(0)
        val attributeCache = mutableMapOf<IElementType, String>()
        val spans = mutableListOf<HtmlChunk>()
        val resolveAfter = arrayOf(GdTypes.COLON, GdTypes.RET, GdTypes.EXTENDS)

        var lastType: IElementType? = null
        while (true) {
            val type = iterator.tokenType
            val style = attributeCache.getOrPut(type) { buildStyle(iterator.textAttributes) }
            @NlsSafe val text = code.substring(iterator.start, iterator.end)
            val block =
                    if (type == GdTypes.IDENTIFIER && resolveAfter.contains(lastType)) GdDocUtil.elementLink(text)
                    else HtmlChunk.text(text)

            spans.add(HtmlChunk.tag("span").style(style).child(block))
            iterator.advance()
            if (iterator.atEnd()) break
            if (type != TokenType.WHITE_SPACE) lastType = type
        }

        return spans
    }

    private fun buildStyle(attributes: TextAttributes): String {
        var style = ""
        val foreground = attributes.foregroundColor
        if (foreground != null) style += "color: ${ColorUtil.toHtmlColor(foreground)};"
        val background = attributes.backgroundColor
        if (background != null) style += "background-color: ${ColorUtil.toHtmlColor(background)};"

        return style
    }

//    fun getHtmlContent(
//        file: PsiFile,
//        text: CharSequence?,
//        ownRangeIterator: SyntaxInfoBuilder.RangeIterator?,
//        schemeToUse: EditorColorsScheme,
//        startOffset: Int,
//        endOffset: Int,
//    ): CharSequence? {
//        var ownRangeIterator = ownRangeIterator
//        val highlighter: EditorHighlighter =
//            HighlighterFactory.createHighlighter(file.viewProvider.virtualFile, schemeToUse, file.project)
//        highlighter.setText(text!!)
//        val highlighterRangeIterator = HighlighterRangeIterator(highlighter, startOffset, endOffset)
//        ownRangeIterator = if (ownRangeIterator == null) highlighterRangeIterator else CompositeRangeIterator(
//            schemeToUse!!,
//            highlighterRangeIterator,
//            ownRangeIterator
//        )
//        return getHtmlContent(text, ownRangeIterator, schemeToUse, endOffset)
//    }
//
//    fun getContent(
//        text: CharSequence,
//        ownRangeIterator: SyntaxInfoBuilder.RangeIterator?,
//        schemeToUse: EditorColorsScheme?,
//        stopOffset: Int,
//    ): CharSequence? {
//        val context = SyntaxInfoBuilder.Context(text, schemeToUse!!, 0)
//        val iterator = SyntaxInfoBuilder.MyMarkupIterator(text, ownRangeIterator!!, schemeToUse)
//        try {
//            context.iterate(iterator, stopOffset)
//        } finally {
//            iterator.dispose()
//        }
//        val info = context.finish()
//        try {
//            SimpleHtmlSyntaxInfoReader(info).use { data ->
//                data.setRawText(text.toString())
//                return data.getBuffer()
//            }
//        } catch (e: IOException) {
//            Logger.getInstance(HtmlSyntaxInfoUtil::class.java).error(e)
//        }
//        return null
//    }

}
