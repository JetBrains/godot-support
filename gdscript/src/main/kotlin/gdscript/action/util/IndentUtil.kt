package gdscript.action.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actions.EditorActionUtil
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.startOffset

object IndentUtil {

    /**
     * Returns indentation of given element
     *  - count: false (default) -> number of spaces (used for EditorActionUtil.indentLine)
     *  - count: true -> number of indents (spaces / tabSize)
     */
    fun Editor.indents(element: PsiElement, count: Boolean = false): Int {
        val line = document.getLineNumber(element.startOffset);
        val indentStart = document.getLineStartOffset(line);
        val indentEnd = EditorActionUtil.findFirstNonSpaceOffsetOnTheLine(document, line);

        val editorSettings = settings;
        val indentSize = indentEnd - indentStart;
        val tabSize = editorSettings.getTabSize(project);

        if (!count) {
            if (editorSettings.isUseTabCharacter(project)) {
                return indentSize * tabSize;
            }

            return indentSize;
        };

        if (editorSettings.isUseTabCharacter(project)) {
            return indentSize;
        }

        return indentSize / tabSize;
    }

}
