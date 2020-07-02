package com.jetbrains.rider.plugins.godot.ideaInterop.fileTypes.tscn

import com.intellij.openapi.fileTypes.PlainTextParserDefinition
import com.intellij.psi.FileViewProvider

class TscnParserDefinition : PlainTextParserDefinition() {
    override fun createFile(viewProvider: FileViewProvider) = TscnFile(viewProvider)
}