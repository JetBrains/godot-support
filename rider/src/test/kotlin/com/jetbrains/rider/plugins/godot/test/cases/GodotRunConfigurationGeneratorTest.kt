package com.jetbrains.rider.plugins.godot.test.cases

import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator.ProtocolListener.ProjectType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunCurrentScene
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunCurrentSceneType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotInEditorDebugRunConfigurationType
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
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
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
                GeneratedConfiguration(
                    GodotPluginBundle.message("godot.debug.in.editor.configuration.name"),
                    GodotInEditorDebugRunConfigurationType::class,
                ),
            ),
        )
    }

    @ParameterizedTest
    @EnumSource(GodotVersion::class, names = ["GODOT_3", "GODOT_4"])
    fun `CMake project creates expected launch configurations`(godotVersion: GodotVersion) {
        val launchType = if (godotVersion == GodotVersion.GODOT_3) {
            GodotDebugRunConfigurationType::class
        } else {
            DotNetExeConfigurationType::class
        }
        assertGeneratedConfigurations(
            godotVersion,
            isPureGdScriptProject = false,
            expected = buildSet {
                add(GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, launchType))
                add(GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, launchType))
                if (godotVersion == GodotVersion.GODOT_4) {
                    add(GeneratedConfiguration(GodotDebugRunCurrentScene.CONFIGURATION_NAME, GodotDebugRunCurrentSceneType::class))
                }
            },
            isCmakeProject = true,
        )
    }

    @ParameterizedTest
    @EnumSource(GodotVersion::class, names = ["GODOT_3", "GODOT_4"])
    fun `CMake GDScript project creates expected launch configurations`(godotVersion: GodotVersion) {
        val launchType = if (godotVersion == GodotVersion.GODOT_3) {
            GodotDebugRunConfigurationType::class
        } else {
            DotNetExeConfigurationType::class
        }
        assertGeneratedConfigurations(
            godotVersion,
            isPureGdScriptProject = true,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, launchType),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, launchType),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, ExeConfigurationType::class),
            ),
            isCmakeProject = true,
        )
    }

    @Test
    fun `Unknown version creates no managed configurations`() {
        assertGeneratedConfigurations(GodotVersion.UNKNOWN, isPureGdScriptProject = false, expected = emptySet())
    }

    @Test
    fun `GDScript project with only a generic path creates the native editor`() {
        assertGeneratedConfigurations(
            GodotVersion.UNKNOWN,
            isPureGdScriptProject = true,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, ExeConfigurationType::class),
            ),
        )
    }

    @Test
    fun `Conflicting paths prefer Godot 4`() {
        assertGeneratedConfigurations(
            GodotVersion.BOTH,
            isPureGdScriptProject = false,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotDebugRunCurrentScene.CONFIGURATION_NAME, GodotDebugRunCurrentSceneType::class),
                GeneratedConfiguration(
                    GodotPluginBundle.message("godot.debug.in.editor.configuration.name"),
                    GodotInEditorDebugRunConfigurationType::class,
                ),
            ),
        )
        assertGeneratedConfigurations(
            GodotVersion.BOTH,
            isPureGdScriptProject = true,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, ExeConfigurationType::class),
            ),
        )
    }

    @Test
    fun `Conflicting paths preserve existing launch configurations`() {
        val runManager = RunManager.getInstance(project)
        runManager.allSettings.toList().forEach(runManager::removeConfiguration)
        val projectType = ProjectType(isPureGdScriptProject = false, isCmakeProject = false)
        generateConfigurations(runManager, GodotVersion.GODOT_3, projectType)
        val existing = runManager.allSettings.filter { it.type is GodotDebugRunConfigurationType }
        runManager.selectedConfiguration = null

        generateConfigurations(runManager, GodotVersion.BOTH, projectType, godotPath = "/other-godot")

        assertEquals(6, runManager.allSettings.size)
        existing.forEach {
            assertSame(it, runManager.allSettings.single { settings -> settings.uniqueID == it.uniqueID })
            assertConfigurationParameters(it)
        }
        assertNull(runManager.findConfigurationByName(GodotRunConfigurationGenerator.ATTACH_CONFIGURATION_NAME))
        assertSame(
            runManager.findConfigurationByName(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME),
            runManager.selectedConfiguration,
        )
    }

    @ParameterizedTest
    @EnumSource(GodotVersion::class)
    fun `Generation cleans up Mono attach only when Godot 4 is known`(godotVersion: GodotVersion) {
        val runManager = RunManager.getInstance(project)
        for (projectType in listOf(
            ProjectType(false, false), ProjectType(true, false), ProjectType(false, true), ProjectType(true, true),
        )) {
            runManager.allSettings.toList().forEach(runManager::removeConfiguration)
            val monoType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
            val monoAttach = runManager.createConfiguration(GodotRunConfigurationGenerator.ATTACH_CONFIGURATION_NAME, monoType.factory)
            val nativeType = ConfigurationTypeUtil.findConfigurationType(ExeConfigurationType::class.java)
            val nativeConfiguration =
                runManager.createConfiguration(GodotRunConfigurationGenerator.ATTACH_CONFIGURATION_NAME, nativeType.factory)
            for (settings in listOf(monoAttach, nativeConfiguration)) {
                settings.storeInLocalWorkspace()
                runManager.addConfiguration(settings)
            }
            runManager.selectedConfiguration = monoAttach

            repeat(2) {
                generateConfigurations(runManager, godotVersion, projectType)

                val monoConfigurations = runManager.allSettings.filter { it.type is MonoRemoteConfigType }
                if (godotVersion == GodotVersion.GODOT_4 || godotVersion == GodotVersion.BOTH) {
                    assertTrue(monoConfigurations.isEmpty())
                    assertTrue(runManager.selectedConfiguration !== monoAttach)
                } else {
                    assertSame(monoAttach, monoConfigurations.single())
                    assertSame(monoAttach, runManager.selectedConfiguration)
                }
                assertSame(nativeConfiguration, runManager.allSettings.single {
                    it.type is ExeConfigurationType && it.name == nativeConfiguration.name
                })
            }
        }
    }

    @ParameterizedTest
    @EnumSource(GodotVersion::class, names = ["GODOT_3", "GODOT_4"])
    fun `Generation preserves saved attach configurations and selection`(godotVersion: GodotVersion) {
        val runManager = RunManager.getInstance(project)
        for (projectType in listOf(ProjectType(false, false), ProjectType(true, false), ProjectType(false, true))) {
            runManager.allSettings.toList().forEach(runManager::removeConfiguration)
            val monoType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
            val monoAttach = runManager.createConfiguration("Custom Mono attach", monoType.factory)
            val customPort = GODOT_3_DEBUG_PORT + 1
            (monoAttach.configuration as RemoteConfiguration).port = customPort
            val inEditorType = ConfigurationTypeUtil.findConfigurationType(GodotInEditorDebugRunConfigurationType::class.java)
            val inEditor = runManager.createConfiguration("Custom editor debug", inEditorType.factory)
            for (settings in listOf(monoAttach, inEditor)) {
                settings.storeInLocalWorkspace()
                runManager.addConfiguration(settings)
            }
            runManager.selectedConfiguration = inEditor

            generateConfigurations(runManager, godotVersion, projectType)

            assertSame(monoAttach, runManager.findConfigurationByName(monoAttach.name))
            assertEquals(customPort, (monoAttach.configuration as RemoteConfiguration).port)
            assertSame(inEditor, runManager.findConfigurationByName(inEditor.name))
            assertSame(inEditor, runManager.selectedConfiguration)
            assertTrue(monoAttach.isStoredInLocalWorkspace)
            assertTrue(inEditor.isStoredInLocalWorkspace)
        }
    }

    private fun assertGeneratedConfigurations(
        godotVersion: GodotVersion,
        isPureGdScriptProject: Boolean,
        expected: Set<GeneratedConfiguration>,
        isCmakeProject: Boolean = false,
    ) {
        val runManager = RunManager.getInstance(project)
        runManager.allSettings.toList().forEach(runManager::removeConfiguration)
        val projectType = ProjectType(isPureGdScriptProject, isCmakeProject)
        generateConfigurations(runManager, GodotVersion.UNKNOWN, projectType, godotPath = null)
        assertTrue(runManager.allSettings.isEmpty())
        assertNull(runManager.selectedConfiguration)

        generateConfigurations(runManager, godotVersion, projectType)

        val actual = runManager.allSettings.mapTo(mutableSetOf()) {
            GeneratedConfiguration(it.name, it.type::class)
        }
        assertEquals(expected, actual)
        assertEquals(expected.size, runManager.allSettings.size)
        runManager.allSettings.forEach(::assertConfigurationParameters)
        assertSame(
            runManager.findConfigurationByName(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME),
            runManager.selectedConfiguration,
        )

        val existing = runManager.allSettings.toList()
        generateConfigurations(runManager, godotVersion, projectType)
        assertEquals(existing, runManager.allSettings)
        existing.forEach { assertSame(it, runManager.allSettings.single { settings -> settings.uniqueID == it.uniqueID }) }
        runManager.allSettings.forEach(::assertConfigurationParameters)
    }

    private fun generateConfigurations(
        runManager: RunManager,
        godotVersion: GodotVersion,
        projectType: ProjectType,
        godotPath: String? = GODOT_PATH,
    ) {
        GodotRunConfigurationGenerator.ProtocolListener().generateConfigurations(
            godot3Path = godotPath.takeIf { godotVersion == GodotVersion.GODOT_3 || godotVersion == GodotVersion.BOTH },
            godot4Path = godotPath.takeIf { godotVersion == GodotVersion.GODOT_4 || godotVersion == GodotVersion.BOTH },
            godotPath,
            runManager,
            project,
            PROJECT_PATH,
            projectType,
            GODOT_3_DEBUG_PORT,
            PROJECT_PATH,
        )
    }

    private fun assertConfigurationParameters(settings: RunnerAndConfigurationSettings) {
        assertTrue(settings.isStoredInLocalWorkspace)
        val configuration = settings.configuration
        if (settings.type is GodotInEditorDebugRunConfigurationType) {
            assertEquals(0, configuration.beforeRunTasks.size)
            return
        }
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

    enum class GodotVersion {
        UNKNOWN,
        GODOT_3,
        GODOT_4,
        BOTH,
    }

    private companion object {
        const val GODOT_PATH = "/godot"
        const val PROJECT_PATH = "/project"
        const val GODOT_3_DEBUG_PORT = 23685
        const val PLAYER_PROGRAM_PARAMETERS = "--path \"$PROJECT_PATH\""
        const val EDITOR_PROGRAM_PARAMETERS = "$PLAYER_PROGRAM_PARAMETERS --editor"
    }
}
