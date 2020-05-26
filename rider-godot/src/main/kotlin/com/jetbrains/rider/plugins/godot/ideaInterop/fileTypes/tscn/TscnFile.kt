package com.jetbrains.rider.plugins.godot.ideaInterop.fileTypes.tscn

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.impl.source.PsiPlainTextFileImpl

class TscnFile(viewProvider: FileViewProvider) : PsiPlainTextFileImpl(viewProvider) {
    override fun getFileType(): FileType = TscnFileType
    override fun toString(): String = "TscnFile:$name"
    override fun getLanguage(): Language = TscnLanguage
}