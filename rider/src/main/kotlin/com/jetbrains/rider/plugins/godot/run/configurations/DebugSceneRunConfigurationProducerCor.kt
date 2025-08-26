package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.runConfigurationType
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationFactory
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntimeType
import java.nio.file.Path
import kotlin.io.path.exists

class DebugSceneRunConfigurationProducerCor : LazyRunConfigurationProducer<DotNetExeConfiguration>() {
    override fun getConfigurationFactory(): DotNetExeConfigurationFactory = runConfigurationType<DotNetExeConfigurationType>().factory

    override fun isConfigurationFromContext(configuration: DotNetExeConfiguration, context: ConfigurationContext): Boolean {
        val basePath = GodotProjectDiscoverer.getInstance(context.project).godotDescriptor.valueOrNull?.mainProjectBasePath ?: return false
        if (GodotProjectDiscoverer.getInstance(context.project).godot4Path.value == null) return false

        val resPath = extractResPath(Path.of(basePath), context) ?: return false
        return configuration.parameters.programParameters.contains(resPath)
    }

    override fun setupConfigurationFromContext(configuration: DotNetExeConfiguration, context: ConfigurationContext, sourceElement: Ref<PsiElement>): Boolean {
        val file = getContainingFile(context) ?: return false
        val descriptor = GodotProjectDiscoverer.getInstance(context.project).godotDescriptor.valueOrNull ?: return false
        if (descriptor.isPureGdScriptProject) return false
        val basePath = descriptor.mainProjectBasePath
        val resPath = extractResPath(Path.of(basePath), context) ?: return false

        val path = GodotProjectDiscoverer.getInstance(context.project).godot4Path.value

        if (path == null || !Path.of(path).exists()) {
            return false
        }
        configuration.parameters.exePath = path
        configuration.parameters.programParameters = "--path \"${Path.of(basePath)}\" \"$resPath\""

        configuration.parameters.workingDirectory = basePath
        configuration.parameters.runtimeType = DotNetCoreRuntimeType
        configuration.name = file.name
        return true
    }
}
