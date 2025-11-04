package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.Executor
import com.intellij.execution.configuration.EmptyRunProfileState
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapStartRequest
import org.jdom.Element

data class GdScriptStructuredArguments(
    val request: DapStartRequest,
    val debugServerPort: Int,
    val remainingArguments: String,
    val scene: String,
)

class GdScriptRunConfiguration(name:String, project: Project, factory: ConfigurationFactory)
    : RunConfigurationBase<GdScriptRunFactory.GdScriptDebugConfigurationOptions>(project, factory, name),
      RunConfigurationWithSuppressedDefaultRunAction,
      WithoutOwnBeforeRunSteps {

    var json: String = GdScriptRunFactory.DEFAULT_FULL_JSON
    val structured: GdScriptStructuredArguments
        get() = GdScriptRunConfigurationHelper.parse(json)

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
        val optionsJson = options.json
        json = if (optionsJson.isNullOrBlank() || !hasMeaningfulContent(optionsJson)) {
            GdScriptRunFactory.DEFAULT_FULL_JSON
        } else {
            optionsJson
        }
    }

    private fun hasMeaningfulContent(jsonString: String): Boolean {
        return try {
            val mapper = GdScriptRunConfigJacksonObjectMapper.getService(project).mapper
            val node = mapper.readTree(jsonString)
            // If it's null or not an object/array
            if (node == null || node.isNull()) {
                return false
            }


            // If it's an empty object {}
            if (node.isObject() && node.size() == 0) {
                return false
            }


            // If it's an empty array []
            if (node.isArray() && node.size() == 0) {
                return false
            }


            // Otherwise, has something
            return true
        } catch (e: Exception) {
            false
        }
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = GdScriptRunConfigurationSettingsEditor(project)

}