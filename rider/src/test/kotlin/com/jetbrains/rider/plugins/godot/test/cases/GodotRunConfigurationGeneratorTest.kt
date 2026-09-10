package com.jetbrains.rider.plugins.godot.test.cases

import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.ConfigurationType
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator.ProtocolListener.ProjectType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunCurrentScene
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunCurrentSceneType
import com.jetbrains.rider.projectView.solutionDirectoryPath
import com.jetbrains.rider.run.configurations.RiderConfigurationParametersAware
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationParameters
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.configurations.exe.ExeConfiguration
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationType
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.rider.run.configurations.remote.RemoteConfiguration
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntimeType
import com.jetbrains.rider.test.annotations.Solution
import com.jetbrains.rider.test.annotations.TestSettings
import com.jetbrains.rider.test.enums.BuildTool
import com.jetbrains.rider.test.enums.sdk.SdkVersion
import com.jetbrains.rider.test.junit5.base.PerClassSolutionTestBase
import com.jetbrains.rider.test.shared.constants.TeamCityTags
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import kotlin.io.path.pathString
import kotlin.reflect.KClass

@Solution("ResCompletionTest", slnName = "GodotProject.sln")
@TestSettings(sdkVersion = SdkVersion.LATEST_STABLE, buildTool = BuildTool.SDK)
@Tag(TeamCityTags.Plugins.Godot.General)
@Timeout(30)
class GodotRunConfigurationGeneratorTest : PerClassSolutionTestBase() {
    @Test
    fun `Godot 3 GDScript project creates expected configurations`() {
        assertGeneratedConfigurations(
            GodotVersion.GODOT_3,
            isPureGdScriptProject = true,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, GodotDebugRunConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, GodotDebugRunConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, ExeConfigurationType::class),
            ),
        )
    }

    @Test
    fun `Godot 3 CSharp project creates expected configurations`() {
        assertGeneratedConfigurations(
            GodotVersion.GODOT_3,
            isPureGdScriptProject = false,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.ATTACH_CONFIGURATION_NAME, MonoRemoteConfigType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, GodotDebugRunConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, GodotDebugRunConfigurationType::class),
            ),
        )
    }

    @Test
    fun `Godot 4 GDScript project creates expected configurations`() {
        assertGeneratedConfigurations(
            GodotVersion.GODOT_4,
            isPureGdScriptProject = true,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, ExeConfigurationType::class),
            ),
        )
    }

    @Test
    fun `Godot 4 CSharp project creates expected configurations`() {
        assertGeneratedConfigurations(
            GodotVersion.GODOT_4,
            isPureGdScriptProject = false,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotDebugRunCurrentScene.CONFIGURATION_NAME, GodotDebugRunCurrentSceneType::class),
            ),
        )
    }

    private fun assertGeneratedConfigurations(
        godotVersion: GodotVersion,
        isPureGdScriptProject: Boolean,
        expected: Set<GeneratedConfiguration>,
    ) {
        val runManager = RunManager.getInstance(project)
        runManager.allSettings.toList().forEach(runManager::removeConfiguration)
        val generator = GodotRunConfigurationGenerator.ProtocolListener()
        val projectType = ProjectType(isPureGdScriptProject, isCmakeProject = false)
        // generateGodot4 nullable corePath because it handles both null (initial value) and later found Godot executables.
        // godot3Path for generateGodot3 is also initially null, but the generator does not need to handle it, hence only generateGodot4 is here.
        generator.generateGodot4(
            corePath = null,
            runManager,
            project,
            PROJECT_PATH,
            projectType,
            GODOT_3_DEBUG_PORT,
            PROJECT_PATH,
        )
        when (godotVersion) {
            GodotVersion.GODOT_3 -> generator.generateGodot3(GODOT_PATH, runManager, project, PROJECT_PATH)
            GodotVersion.GODOT_4 -> generator.generateGodot4(
                GODOT_PATH, runManager, project, PROJECT_PATH, projectType, GODOT_3_DEBUG_PORT,
                PROJECT_PATH
            )
        }
        // This path is triggered by both godot4 and godot3 so the ordering between godot3/godot4 and this does not matter.
        generator.generateGodot(GODOT_PATH, runManager, project, PROJECT_PATH, isPureGdScriptProject)

        val actual = runManager.allSettings.mapTo(mutableSetOf()) {
            GeneratedConfiguration(it.name, it.type::class)
        }
        assertEquals(expected, actual)
        runManager.allSettings.forEach(::assertConfigurationParameters)
    }

    private fun assertConfigurationParameters(settings: RunnerAndConfigurationSettings) {
        val configuration = settings.configuration
        if (configuration is RemoteConfiguration) {
            assertEquals(GODOT_3_DEBUG_PORT, configuration.port)
            return
        }

        val parameters = (configuration as RiderConfigurationParametersAware<*>).parameters
        assertEquals(GODOT_PATH, parameters.exePath)
        val expectedProgramParameters = when (settings.name) {
            GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME -> PLAYER_PROGRAM_PARAMETERS
            GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME -> EDITOR_PROGRAM_PARAMETERS
            GodotDebugRunCurrentScene.CONFIGURATION_NAME -> ""
            else -> error("Unexpected configuration: ${settings.name}")
        }
        assertEquals(expectedProgramParameters, parameters.programParameters)

        val expectedWorkingDirectory = if (settings.type is GodotDebugRunCurrentSceneType) {
            PROJECT_PATH
        } else {
            project.solutionDirectoryPath.pathString
        }
        assertEquals(expectedWorkingDirectory, parameters.workingDirectory)

        if (parameters is DotNetExeConfigurationParameters) {
            assertEquals(DotNetCoreRuntimeType, parameters.runtimeType)
        }
        if (settings.type is ExeConfigurationType) {
            assertEquals(0, (configuration as ExeConfiguration).beforeRunTasks.size)
        }
    }

    private data class GeneratedConfiguration(
        val name: String,
        val type: KClass<out ConfigurationType>,
    )

    private enum class GodotVersion {
        GODOT_3,
        GODOT_4,
    }

    private companion object {
        const val GODOT_PATH = "/godot"
        const val PROJECT_PATH = "/project"
        const val GODOT_3_DEBUG_PORT = 23685
        const val PLAYER_PROGRAM_PARAMETERS = "--path \"$PROJECT_PATH\""
        const val EDITOR_PROGRAM_PARAMETERS = "$PLAYER_PROGRAM_PARAMETERS --editor"
    }
}
