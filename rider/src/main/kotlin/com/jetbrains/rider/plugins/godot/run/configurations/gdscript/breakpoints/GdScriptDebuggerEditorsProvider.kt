package com.jetbrains.rider.plugins.godot.run.configurations.gdscript.breakpoints

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProviderBase
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import com.jetbrains.rider.godot.community.gdscript.GdLanguage

internal class GdScriptDebuggerEditorsProvider: XDebuggerEditorsProviderBase() {
    override fun getFileType(): FileType = GdFileType

    override fun createExpressionCodeFragment(project: Project, text: String, context: PsiElement?, isPhysical: Boolean): PsiFile {
        return PsiFileFactory.getInstance(project).createFileFromText("expression.gdscript", GdLanguage, text)
    }
}