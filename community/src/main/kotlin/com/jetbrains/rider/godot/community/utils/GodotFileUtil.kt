package com.jetbrains.rider.godot.community.utils

import com.intellij.openapi.vfs.VirtualFile

object GodotFileUtil {
    const val GD = "gd"
    private const val TSCN = "tscn"

    fun isGdFile(file: VirtualFile?) = isMatchingFile(file, GD)
    fun isTscnFile(file: VirtualFile?) = isMatchingFile(file, TSCN)

    private fun isMatchingFile(file: VirtualFile?, extension: String) = file?.extension?.equals(extension, true) ?: false
}
