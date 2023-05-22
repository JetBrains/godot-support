package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.runConfigurationType
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import java.io.File

class DebugSceneRunConfigurationProducerCor : LazyRunConfigurationProducer<DotNetExeConfiguration>() {
    override fun getConfigurationFactory() = runConfigurationType<DotNetExeConfigurationType>().factory

    override fun isConfigurationFromContext(configuration: DotNetExeConfiguration, context: ConfigurationContext): Boolean {
        if (GodotProjectDiscoverer.getInstance(context.project).mainProjectBasePath.value == null) return false
        if (GodotProjectDiscoverer.getInstance(context.project).godotCorePath.value == null) return false

        val resPath = DebugSceneRunConfigurationProducer.extractResPath(GodotProjectDiscoverer.getInstance(context.project).mainProjectBasePath.value!!, context) ?: return false
        return configuration.parameters.programParameters.contains(resPath)
    }

    override fun setupConfigurationFromContext(configuration: DotNetExeConfiguration, context: ConfigurationContext, sourceElement: Ref<PsiElement>): Boolean {
        val file = DebugSceneRunConfigurationProducer.getContainingFile(context) ?: return false
        val resPath = DebugSceneRunConfigurationProducer.extractResPath(GodotProjectDiscoverer.getInstance(context.project).mainProjectBasePath.value!!, context) ?: return false

        val path = GodotProjectDiscoverer.getInstance(context.project).godotCorePath.value

        if (path == null || !File(path).exists()) {
            return false
        }
        configuration.parameters.exePath = path
        configuration.parameters.programParameters = "--path \"${GodotProjectDiscoverer.getInstance(context.project).mainProjectBasePath.value}\" \"$resPath\""

        configuration.parameters.workingDirectory = "${context.project.basePath}"
        configuration.name = file.name
        return true
    }
}
