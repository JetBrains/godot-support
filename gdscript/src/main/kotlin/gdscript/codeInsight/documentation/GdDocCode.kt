package gdscript.codeInsight.documentation

import com.intellij.ide.highlighter.HighlighterFactory
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.openapi.editor.richcopy.SyntaxInfoBuilder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import gdscript.GdLanguage
import gdscript.highlighter.GdSyntaxHighlighter
import gdscript.highlighter.GdSyntaxHighlighterFactory
import java.io.IOException


object GdDocCode {

    fun createHighlightedSnippet(code: String, project: Project) {
        val scheme = EditorColorsManager.getInstance().globalScheme
        val parsedCode = code.replace("\t", "  ")
        val psi = PsiFileFactory.getInstance(project).createFileFromText(GdLanguage, parsedCode)

        val highlighter = HighlighterFactory.createHighlighter(psi.virtualFile, scheme, project)
        highlighter.setText(parsedCode)

        val iterator = highlighter.createIterator(0)

        //GdSyntaxHighlighter().getTokenHighlights()
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
