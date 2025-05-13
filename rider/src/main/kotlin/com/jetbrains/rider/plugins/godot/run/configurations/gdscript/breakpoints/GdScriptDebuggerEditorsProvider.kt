package com.jetbrains.rider.plugins.godot.run.configurations.gdscript.breakpoints

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProviderBase
import gdscript.GdFileType

internal class GdScriptDebuggerEditorsProvider: XDebuggerEditorsProviderBase() {
    override fun getFileType(): FileType {
        return GdFileType.INSTANCE
    }

    override fun createExpressionCodeFragment(project: Project, text: String, context: PsiElement?, isPhysical: Boolean): PsiFile {
        return PsiFileFactory.getInstance(project).createFileFromText("expression.gdscript", PlainTextLanguage.INSTANCE, text)
    }
}