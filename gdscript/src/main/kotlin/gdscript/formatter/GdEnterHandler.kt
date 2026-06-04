package gdscript.formatter

import com.intellij.application.options.CodeStyle
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.ide.DataManager
import com.intellij.injected.editor.EditorWindow
import com.intellij.lang.ASTNode
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.actions.SplitLineAction
import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiUtilCore
import gdscript.formatter.block.GdBlocks
import gdscript.psi.GdFile
import gdscript.psi.GdTypes

/**
 * Port of `PythonEnterHandler` adapted for GDScript.
 *
 * Three behaviours:
 *   1. Bracket guard — when caret is directly inside `(` `[` `{`, bail out so the formatter's
 *      continuation indent runs.
 *   2. String-literal continuation — splitting a string across lines. GDScript has no implicit
 *      string concatenation, so the delimiter always includes a `+`. Outside brackets it also
 *      includes a trailing `\` for line continuation; inside brackets (or when
 *      [GdCodeStyleSettings.PARENTHESISE_ON_ENTER] is on) the `\` is omitted.
 */
class GdEnterHandler : EnterHandlerDelegate {

    private var postprocessShift = 0

    override fun postProcessEnter(
        file: PsiFile,
        editor: Editor,
        dataContext: DataContext,
    ): EnterHandlerDelegate.Result {
        if (file !is GdFile) return EnterHandlerDelegate.Result.Continue
        if (postprocessShift > 0) {
            editor.caretModel.moveCaretRelatively(postprocessShift, 0, false, false, false)
            postprocessShift = 0
        }
        return EnterHandlerDelegate.Result.Continue
    }

    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffset: Ref<Int>,
        caretAdvance: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?,
    ): EnterHandlerDelegate.Result {
        var resolvedFile = file
        var resolvedEditor = editor
        var offset = caretOffset.get()
        if (editor is EditorWindow) {
            resolvedFile = InjectedLanguageManager.getInstance(file.project).getTopLevelFile(file)
            resolvedEditor = InjectedLanguageUtil.getTopLevelEditor(editor)
            offset = resolvedEditor.caretModel.offset
        }
        if (resolvedFile !is GdFile) return EnterHandlerDelegate.Result.Continue
        if (DataManager.getInstance().loadFromDataContext(dataContext, SplitLineAction.SPLIT_LINE_KEY) != null) {
            return EnterHandlerDelegate.Result.Continue
        }

        val doc = resolvedEditor.document
        PsiDocumentManager.getInstance(resolvedFile.project).commitDocument(doc)

        val element = PsiUtilCore.getElementAtOffset(resolvedFile, offset)
        if (element === resolvedFile) return EnterHandlerDelegate.Result.Continue
        val prevElement = if (offset > 0) PsiUtilCore.getElementAtOffset(resolvedFile, offset - 1) else null

        // Bracket guard — caret directly between brackets like `(<caret>)`, `[<caret>]`, `{<caret>}`.
        val nodeType = element.node.elementType
        if (nodeType in LEFT_BRACKETS || nodeType in RIGHT_BRACKETS) {
            val container = element.parent
            if (container != null && container.node.elementType in GdBlocks.BRACKETED_INDENT_PARENTS) {
                return EnterHandlerDelegate.Result.Continue
            }
        }

        val parenthesiseOnEnter = CodeStyle.getCustomSettings(resolvedFile, GdCodeStyleSettings::class.java)
            .PARENTHESISE_ON_ENTER

        // String split.
        val stringLeaf = findStringLeafAtCaret(element, prevElement, offset)
        if (stringLeaf != null) {
            val result = tryStringSplit(stringLeaf, offset, doc, caretOffset, parenthesiseOnEnter)
            if (result != null) return result
        }

        return EnterHandlerDelegate.Result.Continue
    }

    private fun tryStringSplit(
        stringLeaf: PsiElement,
        offset: Int,
        doc: Document,
        caretOffset: Ref<Int>,
        parenthesiseOnEnter: Boolean,
    ): EnterHandlerDelegate.Result? {
        val stringText = stringLeaf.text
        val stringStart = stringLeaf.textRange.startOffset
        val intrinsicPrefix = when {
            stringText.startsWith("$") -> "$"
            stringText.startsWith("^") -> "^"
            stringText.startsWith("r") -> "r"
            else -> ""
        }
        if (stringText.length <= intrinsicPrefix.length) return null
        val firstQuote = stringText[intrinsicPrefix.length]
        if (firstQuote != '"' && firstQuote != '\'') return null
        val quote = firstQuote.toString()

        // Triple-quoted strings — leave alone (parser bugs)
        if (stringText.length >= intrinsicPrefix.length + 3 &&
            stringText.regionMatches(intrinsicPrefix.length, quote.repeat(3), 0, 3)
        ) {
            return EnterHandlerDelegate.Result.Continue
        }

        val quoteOffset = stringStart + intrinsicPrefix.length
        // Caret in the prefix or at the opening quote — bail.
        if (offset <= quoteOffset) return EnterHandlerDelegate.Result.Continue

        val docChars = doc.charsSequence

        // Don't split mid escape sequence.
        val afterBackslash = offset > 0 && docChars[offset - 1] == '\\'
        val isEscapedQuote = afterBackslash && offset < doc.textLength && docChars[offset] == firstQuote
        val isEscapedBackslash = afterBackslash && offset >= 2 && docChars[offset - 2] == '\\'
        if (afterBackslash && !isEscapedQuote && !isEscapedBackslash) {
            return EnterHandlerDelegate.Result.Continue
        }
        postprocessShift = intrinsicPrefix.length + quote.length // TODO: Why does Python place it differently here?

        // GDScript needs an explicit `+` between fragments (no implicit concat).
        val insideImplicitWrap = isCaretInsideBracketedIndentParent(stringLeaf, offset)
        val delimiter = if (parenthesiseOnEnter || insideImplicitWrap) " +" else " + \\"

        var insertionOffset = offset
        if (insideImplicitWrap) {
            doc.insertString(insertionOffset, quote + delimiter + intrinsicPrefix + quote)
            caretOffset.set(caretOffset.get() + delimiter.length + 1)
            return EnterHandlerDelegate.Result.Continue
        }

        if (isEscapedQuote) {
            caretOffset.set(caretOffset.get() + 1)
            insertionOffset++
        }
        if (parenthesiseOnEnter) {
            parenthesiseRange(stringLeaf.textRange, doc)
            caretOffset.set(caretOffset.get() + 1)
            insertionOffset++
        }
        doc.insertString(insertionOffset, quote + delimiter + intrinsicPrefix + quote)
        caretOffset.set(caretOffset.get() + delimiter.length + 1)
        return EnterHandlerDelegate.Result.Continue
    }

    private fun findStringLeafAtCaret(element: PsiElement, prevElement: PsiElement?, offset: Int): PsiElement? {
        val atCaretIsString = element.node.elementType == GdTypes.STRING
        val prevIsString = prevElement?.node?.elementType == GdTypes.STRING
        return when {
            atCaretIsString && offset > element.textRange.startOffset -> element
            prevIsString && prevElement.textRange.endOffset > offset -> prevElement
            else -> null
        }
    }

    private fun isCaretInsideBracketedIndentParent(element: PsiElement, offset: Int): Boolean {
        var node: ASTNode? = element.node
        while (node != null) {
            if (node.elementType in GdBlocks.BRACKETED_INDENT_PARENTS) {
                val range = node.textRange
                if (range.startOffset < offset && offset < range.endOffset) return true
            }
            node = node.treeParent
        }
        return false
    }

    private fun parenthesiseRange(range: TextRange, doc: Document) {
        doc.insertString(range.endOffset, ")")
        doc.insertString(range.startOffset, "(")
    }

    companion object {
        private val LEFT_BRACKETS: TokenSet = TokenSet.create(GdTypes.LRBR, GdTypes.LSBR, GdTypes.LCBR)
        private val RIGHT_BRACKETS: TokenSet = TokenSet.create(GdTypes.RRBR, GdTypes.RSBR, GdTypes.RCBR)

        private val STATEMENT_TYPES: TokenSet = TokenSet.create(
            GdTypes.IF_ST, GdTypes.ELIF_ST, GdTypes.ELSE_ST,
            GdTypes.FOR_ST, GdTypes.WHILE_ST,
            GdTypes.MATCH_ST, GdTypes.MATCH_BLOCK,
            GdTypes.METHOD_DECL_TL, GdTypes.CLASS_DECL_TL,
            GdTypes.VAR_DECL_ST, GdTypes.CLASS_VAR_DECL_TL,
            GdTypes.CONST_DECL_ST, GdTypes.CONST_DECL_TL,
            GdTypes.ASSIGN_ST, GdTypes.EXPR_ST, GdTypes.FLOW_ST,
            GdTypes.SIGNAL_DECL_TL,
            GdTypes.ANNOTATION_TL, GdTypes.ANNOTATION_STMT,
            GdTypes.GET_DECL, GdTypes.SET_DECL,
        )
    }
}
