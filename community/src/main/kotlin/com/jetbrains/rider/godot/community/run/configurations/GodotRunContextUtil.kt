package com.jetbrains.rider.godot.community.run.configurations

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.psi.PsiFile
import com.jetbrains.rider.godot.community.utils.GodotFileUtil
import java.nio.file.Path
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.relativeTo

object GodotRunContextUtil {
    fun getTscnFileFromContext(context: ConfigurationContext): PsiFile? {
        val location = context.psiLocation ?: return null
        val file = location.containingFile ?: return null
        if (!GodotFileUtil.isTscnFile(file.virtualFile)) return null
        return file
    }

    fun getSceneResPathFromContext(basePath: Path, context: ConfigurationContext): String? {
        val file = getTscnFileFromContext(context) ?: return null
        val relPath = file.virtualFile.toNioPath().relativeTo(basePath).invariantSeparatorsPathString
        return "res://$relPath"
    }
}
