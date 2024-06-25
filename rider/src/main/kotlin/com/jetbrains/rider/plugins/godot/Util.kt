package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.vfs.VirtualFile

class Util {
    companion object {
        private const val GD = "gd"
        private const val TSCN = "tscn"
        private const val CSHARP = "cs"
        const val GDSCRIPT = "GDScript"

        fun isGdFile(file: VirtualFile?) = isMatchingFile(file, GD)
        fun isTscnFile(file: VirtualFile?) = isMatchingFile(file, TSCN)

        fun isCSharpFile(file: VirtualFile?) = isMatchingFile(file, CSHARP)

        private fun isMatchingFile(file: VirtualFile?, extension: String) = file?.extension?.equals(extension, true) ?: false
    }
}