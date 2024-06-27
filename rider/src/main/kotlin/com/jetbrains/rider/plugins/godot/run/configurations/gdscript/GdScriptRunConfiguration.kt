package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.Executor
import com.intellij.execution.configuration.EmptyRunProfileState
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction
import com.intellij.execution.ui.FragmentedSettings
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapLaunchArgumentsProvider
import com.intellij.platform.dap.DapStartRequest
import com.intellij.util.xmlb.XmlSerializer
import com.intellij.util.xmlb.annotations.Attribute
import com.intellij.util.xmlb.annotations.Transient
import com.jetbrains.rider.run.configurations.remote.RemoteConfiguration
import org.jdom.Element

class GdScriptRunConfiguration(name:String, project: Project, factory: ConfigurationFactory)
    : RunConfigurationBase<Element>(project, factory, name),
      RunConfigurationWithSuppressedDefaultRunAction,
      RemoteConfiguration,
      WithoutOwnBeforeRunSteps, DapLaunchArgumentsProvider {

    override val adapterId = GdScriptDebugAdapter
    override val request: DapStartRequest = DapStartRequest.Launch

    @Attribute
    override var port: Int = 6006

    @Transient
    override var address: String = "127.0.0.1"

    @Transient
    override var listenPortForConnections: Boolean = false

    override fun arguments(): Map<String, Any?> = mapOf(
        // all those do not affect anything
    )

    override var selectedOptions: MutableList<FragmentedSettings.Option>
        get() = super.selectedOptions
        set(value) {}

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        // todo: We can start the Godot Editor here
        return EmptyRunProfileState.INSTANCE
    }

    override fun clone(): RunConfiguration {
        val configuration = super.clone() as GdScriptRunConfiguration
        configuration.port = port
        return configuration
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        XmlSerializer.deserializeInto(this, element)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        XmlSerializer.serializeInto(this, element)
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = GdScriptRunConfigurationSettingsEditor(project)

}