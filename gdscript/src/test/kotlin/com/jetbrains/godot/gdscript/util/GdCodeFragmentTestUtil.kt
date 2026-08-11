package com.jetbrains.godot.gdscript.util

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiElement
import com.intellij.testFramework.EditorTestUtil
import com.intellij.testFramework.TestModeFlags
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import gdscript.dap.breakpoints.GdScriptDebuggerEditorsProvider
import gdscript.psi.GdPsiCodeFragment

fun CodeInsightTestFixture.gdFragment(text: String, context: PsiElement? = null): GdPsiCodeFragment =
    GdScriptDebuggerEditorsProvider()
        .createExpressionCodeFragment(project, text, context, true) as GdPsiCodeFragment

/** Opens the evaluate window on a fragment: afterwards every ordinary `myFixture` call applies to it. */
fun CodeInsightTestFixture.configureGdFragment(text: String, context: PsiElement? = null): GdPsiCodeFragment {
    val fragment = gdFragment(text, context)
    val virtualFile = requireNotNull(fragment.virtualFile) { "a physical fragment must have a VirtualFile" }
    configureFromExistingVirtualFile(virtualFile)
    return fragment
}

/**
 * The element the fragment pretends to have been typed at, marked by [EditorTestUtil.CARET_TAG].
 */
fun CodeInsightTestFixture.gdContextAt(fileName: String, source: String): PsiElement {
    val caret = source.indexOf(EditorTestUtil.CARET_TAG)
    require(caret >= 0) { "no ${EditorTestUtil.CARET_TAG} in the context source of $fileName" }

    val file = addFileToProject(fileName, source.removeRange(caret, caret + EditorTestUtil.CARET_TAG.length))
    return requireNotNull(file.findElementAt(caret)) { "no element at ${EditorTestUtil.CARET_TAG} in $fileName" }
}

/** A lookup that boils down to a single item is otherwise inserted straight away, leaving nothing to inspect. */
fun disableSingleItemAutoInsert(disposable: Disposable) {
    val settings = CodeInsightSettings.getInstance()
    val previous = settings.AUTOCOMPLETE_ON_CODE_COMPLETION
    settings.AUTOCOMPLETE_ON_CODE_COMPLETION = false
    Disposer.register(disposable) { settings.AUTOCOMPLETE_ON_CODE_COMPLETION = previous }
}

/**
 * GDScript annotations are downgraded to INFORMATION outside this flag - see `newAnnotationGd` - so a severity
 * filter would find nothing without it.
 */
fun preserveGdAnnotatorSeverity(disposable: Disposable) {
    TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, disposable)
}
