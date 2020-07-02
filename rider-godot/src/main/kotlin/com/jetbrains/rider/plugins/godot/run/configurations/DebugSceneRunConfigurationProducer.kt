package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.runConfigurationType
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.PsiElement
import com.jetbrains.rider.plugins.godot.ideaInterop.fileTypes.tscn.TscnFile

class DebugSceneRunConfigurationProducer : LazyRunConfigurationProducer<GodotDebugRunConfiguration>() {
    override fun getConfigurationFactory() = runConfigurationType<GodotDebugConfigurationType>().debugFactory

    override fun isConfigurationFromContext(configuration: GodotDebugRunConfiguration, context: ConfigurationContext): Boolean {
        val sceneName = extractSceneName(context) ?: return false
        return configuration.godotScene == sceneName
    }

    override fun setupConfigurationFromContext(configuration: GodotDebugRunConfiguration,
                                               context: ConfigurationContext,
                                               sourceElement: Ref<PsiElement>): Boolean {
        val file = getContainingFile(context) ?: return false
        val sceneName = extractSceneName(context) ?: return false
        configuration.godotScene = sceneName
        configuration.name = file.name
        return true
    }

    private fun extractSceneName(context: ConfigurationContext): String? {
        val file = getContainingFile(context) ?: return null
        val project = context.project ?: return null
        val root = ProjectRootManager.getInstance(project).fileIndex.getContentRootForFile(file.virtualFile) ?: return null
        val filename = FileUtil.getRelativePath(root.path, file.virtualFile.path, '/')
        return "res://$filename"
    }

    private fun getContainingFile(context: ConfigurationContext):TscnFile? {
        val location = context.psiLocation ?: return null
        val file = location.containingFile as? TscnFile ?: return null
        return file
    }
}
