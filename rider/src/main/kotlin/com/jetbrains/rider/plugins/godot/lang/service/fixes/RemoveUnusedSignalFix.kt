package com.jetbrains.rider.plugins.godot.lang.service.fixes

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.platform.lsp.util.getRangeInDocument
import com.intellij.psi.PsiFile
import org.eclipse.lsp4j.Diagnostic

/**
 * Quick fix for removing unused signal declarations in GDScript via LSP diagnostics.
 */
class RemoveUnusedSignalFix(
    private val diagnostic: Diagnostic
) : IntentionAction {
    
    override fun getText(): String {
        return "Remove unused signal"
    }
    
    override fun getFamilyName(): String {
        return "Remove unused signal"
    }
    
    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return editor != null && file != null
    }

    // in this case QuickFix is easy
    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor == null || file == null) return
        
        val document = editor.document
        val textRange = getRangeInDocument(document, diagnostic.range) ?: return
        
        // Find the entire line containing the signal declaration
        val startLine = document.getLineNumber(textRange.startOffset)
        val lineStartOffset = document.getLineStartOffset(startLine)
        val lineEndOffset = document.getLineEndOffset(startLine)
        
        // Delete the entire line including the newline
        val deleteRange = if (lineEndOffset < document.textLength) {
            TextRange(lineStartOffset, lineEndOffset + 1)
        } else {
            // Last line - also check if we should remove the preceding newline
            if (lineStartOffset > 0) {
                TextRange(lineStartOffset - 1, lineEndOffset)
            } else {
                TextRange(lineStartOffset, lineEndOffset)
            }
        }
        
        document.deleteString(deleteRange.startOffset, deleteRange.endOffset)
    }
    
    override fun startInWriteAction(): Boolean = true
}
