package gdscript.dap

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import kotlinx.coroutines.launch

@Service(Service.Level.PROJECT)
class GdScriptRunConfigurationGenerator(project: Project) {

    init {
        val scope = GdScriptProjectLifetimeService.getScope(project)
        val runManager = RunManager.getInstance(project)
        scope.launch {
            GodotCommunityUtil.getGodotExecutablePathFlow(project).collect { path ->
                cleanupLegacyConfigs(runManager)
                if (path != null) {
                    generateConfigs(runManager)
                    selectConfigurationIfNeeded(runManager)
                }
            }
        }
    }

    companion object {
        const val PLAYER_GDSCRIPT_CONFIGURATION_NAME: String = "Player (GDScript)"
        const val CURRENT_SCENE_GDSCRIPT_CONFIGURATION_NAME: String = "Player (Current Scene, GDScript)"
        const val PLAYER_GDSCRIPT_ATTACH_CONFIGURATION_NAME: String = "Debug GDScript (Running session)"
        private const val LEGACY_PLAYER_GDSCRIPT_CONFIGURATION_NAME: String = "Player GDScript"
        private const val LEGACY_CURRENT_SCENE_GDSCRIPT_CONFIGURATION_NAME: String = "Current Scene GDScript"

        private fun cleanupLegacyConfigs(runManager: RunManager) {
            renameLegacyConfiguration(
                runManager,
                LEGACY_PLAYER_GDSCRIPT_CONFIGURATION_NAME,
                PLAYER_GDSCRIPT_CONFIGURATION_NAME,
            )
            renameLegacyConfiguration(
                runManager,
                LEGACY_CURRENT_SCENE_GDSCRIPT_CONFIGURATION_NAME,
                CURRENT_SCENE_GDSCRIPT_CONFIGURATION_NAME,
            )

            // todo: remove in 271
            val toRemove = runManager.allSettings.filter {
                it.type is GdScriptConfigurationType &&
                (it.name == LEGACY_PLAYER_GDSCRIPT_CONFIGURATION_NAME ||
                 it.name == LEGACY_CURRENT_SCENE_GDSCRIPT_CONFIGURATION_NAME)
            }
            for (value in toRemove) {
                runManager.removeConfiguration(value)
            }

            val legacyNativeConfigs = runManager.allSettings.filter {
                it.type.id == "RunNativeExe" &&
                (it.name == LEGACY_PLAYER_GDSCRIPT_CONFIGURATION_NAME || it.name == LEGACY_CURRENT_SCENE_GDSCRIPT_CONFIGURATION_NAME)
            }
            for (value in legacyNativeConfigs) {
                runManager.removeConfiguration(value)
            }
        }

        private fun renameLegacyConfiguration(runManager: RunManager, legacyName: String, newName: String) {
            val legacyConfigurations = runManager.allSettings.filter {
                it.type is GdScriptConfigurationType && it.name == legacyName
            }
            if (legacyConfigurations.isEmpty()) return

            val newConfigurationExists = runManager.allSettings.any {
                it.type is GdScriptConfigurationType && it.name == newName
            }
            if (newConfigurationExists) {
                legacyConfigurations.forEach(runManager::removeConfiguration)
            }
            else {
                legacyConfigurations.first().name = newName
                legacyConfigurations.drop(1).forEach(runManager::removeConfiguration)
            }
        }

        private fun generateConfigs(runManager: RunManager) {
            createOrUpdateGdScriptRunConfiguration(PLAYER_GDSCRIPT_CONFIGURATION_NAME, runManager)
            createOrUpdateGdScriptRunConfiguration(CURRENT_SCENE_GDSCRIPT_CONFIGURATION_NAME, runManager, scene = "current")
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
            scene: String = "main",
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
                var json = GdScriptRunFactory.DEFAULT_FULL_JSON.replace("\"scene\" : \"main\"", "\"scene\" : \"$scene\"")
                if (attach) json = json.replace("\"request\" : \"Launch\"", "\"request\" : \"Attach\"")
                (runConfiguration.configuration as GdScriptRunConfiguration).json = json
                runConfiguration.storeInLocalWorkspace()
                runManager.addConfiguration(runConfiguration)
            }
        }

        fun getService(project: Project) {
            project.getService(GdScriptRunConfigurationGenerator::class.java)
        }
    }

    class Activity : ProjectActivity {
        override suspend fun execute(project: Project) {
            getService(project)
        }
    }
}
