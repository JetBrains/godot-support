package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.vfs.VirtualFile

class Util {
    companion object {
        const val GD = "gd"
        private const val TSCN = "tscn"

        fun isGdFile(file: VirtualFile?) = isMatchingFile(file, GD)
        fun isTscnFile(file: VirtualFile?) = isMatchingFile(file, TSCN)

        private fun isMatchingFile(file: VirtualFile?, extension: String) = file?.extension?.equals(extension, true) ?: false
    }
}