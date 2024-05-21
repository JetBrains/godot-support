package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.BeforeRunTask
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.jetbrains.rider.build.tasks.BuildSolutionBeforeRunTask
import com.jetbrains.rider.build.tasks.BuildSolutionBeforeRunTaskProvider
import org.jetbrains.annotations.NotNull

open class GdScriptRunFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId() = "GdScriptRunFactory"

    override fun configureBeforeRunTaskDefaults(providerID: Key<out BeforeRunTask<BeforeRunTask<*>>>?,
                                                task: BeforeRunTask<out BeforeRunTask<*>>?) {
        if (providerID == BuildSolutionBeforeRunTaskProvider.providerId && task is BuildSolutionBeforeRunTask) {
            task.isEnabled = false
        }
    }

    override fun createTemplateConfiguration(@NotNull project: Project): RunConfiguration =
        GdScriptRunConfiguration(this.name, project, this)
}