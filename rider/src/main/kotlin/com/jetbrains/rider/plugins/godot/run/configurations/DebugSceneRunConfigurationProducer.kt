package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.runConfigurationType
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.jetbrains.rider.godot.community.run.configurations.GodotRunContextUtil
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import java.nio.file.Path
import kotlin.io.path.exists

class DebugSceneRunConfigurationProducer : LazyRunConfigurationProducer<GodotDebugRunConfiguration>() {
    override fun getConfigurationFactory(): GodotDebugRunFactory = runConfigurationType<GodotDebugRunConfigurationType>().factory

    override fun isConfigurationFromContext(configuration: GodotDebugRunConfiguration, context: ConfigurationContext): Boolean {
        val descriptor = GodotProjectDiscoverer.getInstance(context.project).godotDescriptor.valueOrNull ?: return false
        if (descriptor.isPureGdScriptProject) return false
        val basePath = descriptor.mainProjectBasePath
        if (GodotProjectDiscoverer.getInstance(context.project).godot3Path.value == null) return false

        val resPath = GodotRunContextUtil.getSceneResPathFromContext(Path.of(basePath), context) ?: return false
        return configuration.parameters.programParameters.contains(resPath)
    }

    override fun setupConfigurationFromContext(configuration: GodotDebugRunConfiguration,
                                               context: ConfigurationContext,
                                               sourceElement: Ref<PsiElement>): Boolean {
        val file = GodotRunContextUtil.getTscnFileFromContext(context) ?: return false
        val basePath = GodotProjectDiscoverer.getInstance(context.project).godotDescriptor.valueOrNull?.mainProjectBasePath ?: return false
        val resPath = GodotRunContextUtil.getSceneResPathFromContext(Path.of(basePath), context) ?: return false

        val path = GodotProjectDiscoverer.getInstance(context.project).godot3Path.value

        if (path == null || !Path.of(path).exists()) {
            return false
        }
        configuration.parameters.exePath = path
        configuration.parameters.programParameters = "--path \"${basePath}\" \"$resPath\""

        configuration.parameters.workingDirectory = basePath
        configuration.name = file.name
        return true
    }
}
