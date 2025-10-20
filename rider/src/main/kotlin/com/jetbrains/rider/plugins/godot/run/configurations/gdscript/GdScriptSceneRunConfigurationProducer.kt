package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.runConfigurationType
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.run.configurations.extractResPath
import com.jetbrains.rider.plugins.godot.run.configurations.getContainingFile
import java.nio.file.Path

class GdScriptSceneRunConfigurationProducer : LazyRunConfigurationProducer<GdScriptRunConfiguration>() {
    override fun getConfigurationFactory() = runConfigurationType<GdScriptConfigurationType>().factory

    override fun isConfigurationFromContext(configuration: GdScriptRunConfiguration, context: ConfigurationContext): Boolean {
        val descriptor = GodotProjectDiscoverer.getInstance(context.project).godotDescriptor.valueOrNull ?: return false
        if (!descriptor.isPureGdScriptProject) return false
        val basePath = descriptor.mainProjectBasePath
        val resPath = extractResPath(Path.of(basePath), context) ?: return false
        return configuration.structured.scene == resPath
    }

    override fun setupConfigurationFromContext(configuration: GdScriptRunConfiguration,
                                               context: ConfigurationContext,
                                               sourceElement: Ref<PsiElement>): Boolean {
        val file = getContainingFile(context) ?: return false
        val descriptor = GodotProjectDiscoverer.getInstance(context.project).godotDescriptor.valueOrNull ?: return false
        if (!descriptor.isPureGdScriptProject) return false
        val basePath = descriptor.mainProjectBasePath
        val resPath = extractResPath(Path.of(basePath), context) ?: return false

        val structured = GdScriptStructuredArguments(
            configuration.structured.request,
            configuration.structured.debugServerPort,
            configuration.structured.remainingArguments,
            resPath
        )
        configuration.json = GdScriptRunConfigurationHelper.serialize(structured)
        configuration.name = file.name
        return true
    }
}
