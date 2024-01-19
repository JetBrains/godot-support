package com.jetbrains.rider.plugins.godot.textMate

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.FileIndentOptionsProvider
import com.jetbrains.rider.plugins.godot.Util

class GdScriptFileIndentOptionsProvider : FileIndentOptionsProvider() {
    override fun getIndentOptions(
        project: Project,
        settings: CodeStyleSettings,
        file: VirtualFile
    ): CommonCodeStyleSettings.IndentOptions? {
        if (Util.isGdFile(file)) {
            val indentOptions = settings.indentOptions
            indentOptions.USE_TAB_CHARACTER = true
            return indentOptions
        }
        return null
    }
}