package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.Executor
import com.intellij.execution.configuration.EmptyRunProfileState
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapLaunchArgumentsProvider
import com.intellij.platform.dap.DapStartRequest
import org.jdom.Element

data class GdScriptStructuredArguments(
    var request: DapStartRequest,
    var debugServerPort: Int,
    var remainingArguments: String,
    var scene: String,
)

class GdScriptRunConfiguration(name:String, project: Project, factory: ConfigurationFactory)
    : RunConfigurationBase<GdScriptRunFactory.GdScriptDebugConfigurationOptions>(project, factory, name),
      RunConfigurationWithSuppressedDefaultRunAction,
      WithoutOwnBeforeRunSteps, DapLaunchArgumentsProvider {

    override val adapterId: GdScriptDebugAdapter = GdScriptDebugAdapter

    var json: String = GdScriptRunFactory.DEFAULT_FULL_JSON
    val structured: GdScriptStructuredArguments
        get() = GdScriptRunConfigurationHelper.parse(json)

    override val request: DapStartRequest
        get() = structured.request

    override fun arguments(): Map<String, Any?> {
        return GdScriptRunConfigurationHelper.parseArgumentsToMap(json)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return EmptyRunProfileState.INSTANCE
    }

    override fun getOptions(): GdScriptRunFactory.GdScriptDebugConfigurationOptions {
        return super.getOptions() as GdScriptRunFactory.GdScriptDebugConfigurationOptions
    }

    override fun writeExternal(element: Element) {
        options.json = json
        super.writeExternal(element)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        json = options.json!!
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = GdScriptRunConfigurationSettingsEditor(project)

}