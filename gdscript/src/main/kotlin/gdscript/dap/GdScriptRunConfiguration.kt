package gdscript.dap

import com.intellij.execution.CantRunException
import com.intellij.execution.Executor
import com.intellij.execution.configuration.EmptyRunProfileState
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.WithoutOwnBeforeRunSteps
import com.intellij.execution.impl.statistics.FusAwareRunConfiguration
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction
import com.intellij.internal.statistic.eventLog.events.EventPair
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapStartRequest
import com.jetbrains.rider.godot.community.GdProjectGodotService
import gdscript.GdScriptBundle
import gdscript.statistics.GdScriptRunConfigurationUsageData
import org.jdom.Element

data class GdScriptStructuredArguments(
    val request: DapStartRequest,
    val debugServerPort: Int,
    val remainingArguments: String,
    val scene: String,
) {
    internal val kind: GdScriptRunConfigurationKind
        get() = when (request) {
            DapStartRequest.Attach -> GdScriptRunConfigurationKind.RUNNING_SESSION
            DapStartRequest.Launch -> if (scene == GdScriptRunFactory.CURRENT_SCENE) {
                GdScriptRunConfigurationKind.CURRENT_SCENE
            }
            else {
                GdScriptRunConfigurationKind.PLAYER
            }
        }
}

internal enum class GdScriptRunConfigurationKind {
    PLAYER,
    CURRENT_SCENE,
    RUNNING_SESSION,
}

class GdScriptRunConfiguration(name: String, project: Project, factory: ConfigurationFactory) :
    RunConfigurationBase<GdScriptRunFactory.GdScriptDebugConfigurationOptions>(project, factory, name),
    RunConfigurationWithSuppressedDefaultRunAction,
    WithoutOwnBeforeRunSteps,
    FusAwareRunConfiguration {

    var json: String = GdScriptRunFactory.DEFAULT_FULL_JSON
    val structured: GdScriptStructuredArguments
        get() = GdScriptRunConfigurationHelper.parse(json)

    override fun getAdditionalUsageData(): List<EventPair<*>> {
        val arguments = GdScriptRunConfigurationHelper.parseForUsage(json) ?: return emptyList()
        return listOf(GdScriptRunConfigurationUsageData.CONFIGURATION_KIND.with(arguments.kind))
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        // Best effort check: for projects with rider-godot plugin and RD connection
        // we provide a check, otherwise the no scene error will appear inside Godot Editor.
        if (name == GdScriptRunConfigurationGenerator.CURRENT_SCENE_GDSCRIPT_CONFIGURATION_NAME &&
            GdProjectGodotService.getInstance(project).currentSceneFlow.value == ""
        ) {
            throw CantRunException(GdScriptBundle.message("run.configuration.error.no.scene"))
        }
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
