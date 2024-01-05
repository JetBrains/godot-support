package com.jetbrains.rider.plugins.godot.textMate

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.FileIndentOptionsProvider

class GdScriptFileIndentOptionsProvider : FileIndentOptionsProvider() {
    override fun getIndentOptions(
        project: Project,
        settings: CodeStyleSettings,
        file: VirtualFile
    ): CommonCodeStyleSettings.IndentOptions? {
        if (file.extension == "gd") {
            val indentOptions = settings.indentOptions
            indentOptions.USE_TAB_CHARACTER = true
            return indentOptions
        }
        return null
    }
}