package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.runConfigurationType
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.rdclient.util.idea.toIOFile
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.projectView.solutionDirectory
import java.io.File

class DebugSceneRunConfigurationProducer : LazyRunConfigurationProducer<GodotDebugRunConfiguration>() {
    companion object{
        internal fun extractResPath(context: ConfigurationContext): String? {
            val file = getContainingFile(context) ?: return null
            val project = context.project ?: return null
            val relPath = file.virtualFile.toIOFile().relativeTo(project.solutionDirectory)
            return "res://$relPath"
        }

        internal fun getContainingFile(context: ConfigurationContext): PsiFile? {
            val location = context.psiLocation ?: return null
            val file = location.containingFile ?: return null
            if (file.virtualFile.extension != "tscn") return null
            return file
        }
    }
    override fun getConfigurationFactory() = runConfigurationType<GodotDebugRunConfigurationType>().factory

    override fun isConfigurationFromContext(configuration: GodotDebugRunConfiguration, context: ConfigurationContext): Boolean {
        if (!GodotProjectDiscoverer.getInstance(context.project).isGodotProject.value) return false
        if (GodotProjectDiscoverer.getInstance(context.project).godotMonoPath.value == null) return false

        val resPath = extractResPath(context) ?: return false
        return configuration.parameters.programParameters.contains(resPath)
    }

    override fun setupConfigurationFromContext(configuration: GodotDebugRunConfiguration,
                                               context: ConfigurationContext,
                                               sourceElement: Ref<PsiElement>): Boolean {
        val file = getContainingFile(context) ?: return false
        val resPath = extractResPath(context) ?: return false

        val path = GodotProjectDiscoverer.getInstance(context.project).godotMonoPath.value

        if (path == null || !File(path).exists()) {
            return false
        }
        configuration.parameters.exePath = path
        configuration.parameters.programParameters = "--path \"${context.project.basePath}\" \"$resPath\""

        configuration.parameters.workingDirectory = "${context.project.basePath}"
        configuration.name = file.name
        return true
    }
}
