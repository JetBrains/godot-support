package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.Executor
import com.intellij.execution.configuration.EmptyRunProfileState
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.FragmentedSettings
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.api.DapLaunchArgumentsProvider
import com.intellij.platform.dap.api.DapStartRequest
import com.intellij.util.xmlb.annotations.Transient
import org.w3c.dom.Element

class GdScriptRunConfiguration(name:String, project: Project, factory: ConfigurationFactory)
    : LocatableConfigurationBase<Element>(project, factory, name),
      WithoutOwnBeforeRunSteps, DapLaunchArgumentsProvider {

    override val adapterId = GdScriptDebugAdapter
    override val request: DapStartRequest = DapStartRequest.Launch
    @Transient
    var port: Int = 6006
    override fun arguments(): Map<String, Any?> = mapOf(
        // all those do not affect anything
    )

    override var selectedOptions: MutableList<FragmentedSettings.Option>
        get() = super.selectedOptions
        set(value) {}

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        // todo: We can start the Godot Editor here
        return EmptyRunProfileState.INSTANCE;
    }

    override fun clone(): RunConfiguration {
        val configuration = super.clone() as GdScriptRunConfiguration
        configuration.port = port
        return configuration
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = GdScriptRunConfigurationSettingsEditor(project)
}