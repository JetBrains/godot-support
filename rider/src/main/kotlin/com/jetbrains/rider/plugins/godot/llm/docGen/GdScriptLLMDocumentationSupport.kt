package com.jetbrains.rider.plugins.godot.llm.docGen

import com.intellij.ml.llm.core.LLMDocumentationSupport
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class GdScriptLLMDocumentationSupport: LLMDocumentationSupport {
    override val docFormatInstruction: String
        get() = "write GDScript doc"

    override val emptyDocumentation: String
        get() = "#"

    override fun buildDocFromSuggestion(suggestion: String): String {
//        TODO("Not yet implemented")
        return "# buildDocFromSuggestion"
    }

    override fun computeDocRange(documentationTarget: PsiElement): TextRange {
        TODO("Not yet implemented")
    }

    override fun containsCompleteDoc(suggestion: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun findDocTargetsInSelection(root: PsiElement, selectionModel: SelectionModel): List<PsiElement> {
        TODO("Not yet implemented")
    }

    override fun findExampleDoc(documentationTarget: PsiElement): Pair<String, String>? {
        TODO("Not yet implemented")
    }

    override fun findNearestDocumentationTarget(element: PsiElement): PsiElement? {
        TODO("Not yet implemented")
    }
}