package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.BeforeRunTask
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationOptions
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.platform.dap.DapStartRequest
import com.intellij.util.xmlb.annotations.Property
import com.jetbrains.rider.build.tasks.BuildSolutionBeforeRunTask
import com.jetbrains.rider.build.tasks.BuildSolutionBeforeRunTaskProvider
import org.jetbrains.annotations.NotNull

open class GdScriptRunFactory(type: ConfigurationType) : ConfigurationFactory(type), DumbAware {
    override fun getId(): String = "GdScriptRunFactory"

    override fun isEditableInDumbMode(): Boolean = true

    override fun getOptionsClass(): Class<GdScriptDebugConfigurationOptions> = GdScriptDebugConfigurationOptions::class.java

    companion object Defaults {
        const val DEFAULT_PORT: Int = 6006
        const val DEFAULT_ADDRESS: String = "127.0.0.1"
        val DEFAULT_REQUEST: DapStartRequest = DapStartRequest.Launch
        const val DEFAULT_SCENE: String = "main"
        val DEFAULT_EMPTY_JSON: String = """
            {
            
            }
        """.trimIndent()
    }

    override fun configureBeforeRunTaskDefaults(providerID: Key<out BeforeRunTask<BeforeRunTask<*>>>?,
                                                task: BeforeRunTask<out BeforeRunTask<*>>?) {
        if (providerID == BuildSolutionBeforeRunTaskProvider.providerId && task is BuildSolutionBeforeRunTask) {
            task.isEnabled = false
        }
    }

    override fun createTemplateConfiguration(@NotNull project: Project): RunConfiguration =
        GdScriptRunConfiguration(this.name, project, this).apply {
            structured.debugServerPort = DEFAULT_PORT
            structured.scene = DEFAULT_SCENE
            structured.request = DEFAULT_REQUEST
            if (structured.remainingArguments.isBlank()) structured.remainingArguments = DEFAULT_EMPTY_JSON
        }

    class GdScriptDebugConfigurationOptions : RunConfigurationOptions() {
        @get:Property
        var json: String? by string("")
    }
}