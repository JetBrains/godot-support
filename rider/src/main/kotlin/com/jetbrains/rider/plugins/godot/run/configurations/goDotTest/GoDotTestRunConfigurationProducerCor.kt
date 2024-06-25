package com.jetbrains.rider.plugins.godot.run.configurations.goDotTest

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.runConfigurationType
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.rdclient.util.idea.toIOFile
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.Util
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntimeType
import java.io.File

class GoDotTestRunConfigurationProducerCor : LazyRunConfigurationProducer<DotNetExeConfiguration>() {

    private fun getContainingFile(context: ConfigurationContext): PsiFile? {
        val location = context.psiLocation ?: return null
        val file = location.containingFile ?: return null
        if (!Util.isCSharpFile(file.virtualFile)) return null
        return file
    }

    private fun extractFileName(context: ConfigurationContext): String? {
        val file = getContainingFile(context) ?: return null
        return file.virtualFile.toIOFile().nameWithoutExtension
    }
    override fun getConfigurationFactory() = runConfigurationType<DotNetExeConfigurationType>().factory

    override fun isConfigurationFromContext(configuration: DotNetExeConfiguration, context: ConfigurationContext): Boolean {
        GodotProjectDiscoverer.getInstance(context.project).godotDescriptor.value?.mainProjectBasePath ?: return false
        if (GodotProjectDiscoverer.getInstance(context.project).godot4Path.value == null) return false

        val testFilePath = extractFileName(context) ?: return false
        return configuration.parameters.programParameters.contains("--run-tests=\"$testFilePath\"")
    }

    override fun setupConfigurationFromContext(configuration: DotNetExeConfiguration, context: ConfigurationContext, sourceElement: Ref<PsiElement>): Boolean {
        val file = getContainingFile(context) ?: return false
        val basePath = GodotProjectDiscoverer.getInstance(context.project).godotDescriptor.value?.mainProjectBasePath ?: return false
        val baseFile = File(basePath)
        val testFilePath = extractFileName(context) ?: return false

        val path = GodotProjectDiscoverer.getInstance(context.project).godot4Path.value

        if (path == null || !File(path).exists()) {
            return false
        }
        configuration.parameters.exePath = path
        configuration.parameters.programParameters = "--run-tests=\"$testFilePath\" --quit-on-finish --path \"${baseFile}\""

        configuration.parameters.workingDirectory = "${context.project.basePath}"
        configuration.parameters.runtimeType = DotNetCoreRuntimeType
        configuration.name = file.name
        return true
    }
}
