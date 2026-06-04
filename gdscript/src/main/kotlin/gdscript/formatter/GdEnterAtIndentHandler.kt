package gdscript.formatter

import com.intellij.codeInsight.editorActions.BackspaceHandler
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.injected.editor.EditorWindow
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil
import gdscript.psi.GdFile

/** Port of `PyEnterAtIndentHandler` — when the caret sits in leading whitespace, suppress auto-indent. */
class GdEnterAtIndentHandler : EnterHandlerDelegate {
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
        if (editor is EditorWindow) {
            resolvedFile = InjectedLanguageManager.getInstance(file.project).getTopLevelFile(file)
            resolvedEditor = InjectedLanguageUtil.getTopLevelEditor(editor)
        }
        if (resolvedFile !is GdFile) return EnterHandlerDelegate.Result.Continue

        if (BackspaceHandler.isWhitespaceBeforeCaret(resolvedEditor)) {
            return EnterHandlerDelegate.Result.DefaultSkipIndent
        }
        return EnterHandlerDelegate.Result.Continue
    }
}
