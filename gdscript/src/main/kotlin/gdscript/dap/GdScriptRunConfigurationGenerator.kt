package gdscript.dap

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import common.util.GdScriptProjectLifetimeService
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

@Service(Service.Level.PROJECT)
class GdScriptRunConfigurationGenerator {

    companion object {
        const val PLAYER_GDSCRIPT_CONFIGURATION_NAME: String = "Player GDScript"
        const val PLAYER_GDSCRIPT_ATTACH_CONFIGURATION_NAME_OLD: String = "Player GDScript (Attach)"
        const val PLAYER_GDSCRIPT_ATTACH_CONFIGURATION_NAME: String = "Debug GDScript (Running session)"

        private fun cleanupLegacyConfigs(runManager: RunManager) {
            // todo: remove in 261
            val toRemove = runManager.allSettings.filter {
                it.type is GdScriptConfigurationType && it.name == PLAYER_GDSCRIPT_ATTACH_CONFIGURATION_NAME_OLD
            }
            for (value in toRemove) {
                runManager.removeConfiguration(value)
            }
        }

        private fun generateConfigs(runManager: RunManager) {
            createOrUpdateGdScriptRunConfiguration(PLAYER_GDSCRIPT_CONFIGURATION_NAME, runManager)
            createOrUpdateGdScriptRunConfiguration(PLAYER_GDSCRIPT_ATTACH_CONFIGURATION_NAME, runManager, attach = true)
        }

        private fun selectConfigurationIfNeeded(runManager: RunManager) {
            if (runManager.selectedConfiguration == null) {
                val config = runManager.findConfigurationByName(PLAYER_GDSCRIPT_CONFIGURATION_NAME)
                if (config != null) {
                    runManager.selectedConfiguration = config
                }
            }
        }

        private fun createOrUpdateGdScriptRunConfiguration(
            configurationName: String,
            runManager: RunManager,
            attach: Boolean = false,
        ) {
            // todo: maybe after some time, we can remove this code, it is only useful to remove previously generated configs
            val toRemove = runManager.allSettings.filter {
                // TODO: Below replaces: `it.type is ExeConfigurationType && it.name == configurationName`
                it.type.id == "RunNativeExe" && it.name == configurationName
            }
            for (value in toRemove) {
                runManager.removeConfiguration(value)
            }

            val configs = runManager.allSettings.filter { it.type is GdScriptConfigurationType && it.name == configurationName }
            if (configs.any()) {
                configs.forEach {
                    // todo: update DAP port, if I find a way to read it from project settings
                }
            } else {
                val configurationType = ConfigurationTypeUtil.findConfigurationType(GdScriptConfigurationType::class.java)
                val runConfiguration = runManager.createConfiguration(configurationName, configurationType.factory)
                if (attach) {
                    (runConfiguration.configuration as GdScriptRunConfiguration).json =
                        GdScriptRunFactory.DEFAULT_FULL_JSON.replace("\"request\" : \"Launch\"", "\"request\" : \"Attach\"")
                }
                runConfiguration.storeInLocalWorkspace()
                runManager.addConfiguration(runConfiguration)
            }
        }
    }

    class Activity : ProjectActivity {
        override suspend fun execute(project: Project) {
            val isGodotDeffered = GodotCommunityUtil.isGodotProject(project) ?: return
            val isGodot = withTimeoutOrNull(30.seconds) { isGodotDeffered.await() } ?: return
            if (!isGodot) return

            val runManager = RunManager.getInstance(project)
            cleanupLegacyConfigs(runManager)

            val scope = GdScriptProjectLifetimeService.getScope(project)
            scope.launch {
                GodotCommunityUtil.getGodotExecutablePathFlow(project).collect { path ->
                    if (path != null) {
                        generateConfigs(runManager)
                        selectConfigurationIfNeeded(runManager)
                    }
                }
            }
        }
    }
}
