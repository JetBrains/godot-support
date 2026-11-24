package com.jetbrains.rider.plugins.godot.test.cases

import com.intellij.execution.DefaultExecutionTarget
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.compound.CompoundRunConfiguration
import com.intellij.execution.compound.CompoundRunConfigurationType
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.jetbrains.rider.build.tasks.BuildSolutionBeforeRunTask
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator.ProtocolListener.ProjectType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunCurrentScene
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunCurrentSceneType
import com.jetbrains.rider.plugins.godot.run.configurations.GodotPlayerConfigurationType
import com.jetbrains.rider.projectView.solutionDirectoryPath
import com.jetbrains.rider.run.configurations.RiderConfigurationParametersAware
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationParameters
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.run.configurations.exe.ExeConfiguration
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationType
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.rider.run.configurations.remote.RemoteConfiguration
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntimeType
import com.jetbrains.rider.test.annotations.Solution
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.TestSettings
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.base.PerClassSolutionTestBase
import com.jetbrains.rider.test.enums.BuildTool
import com.jetbrains.rider.test.enums.sdk.SdkVersion
import com.jetbrains.rider.test.reporting.SubsystemConstants
import com.jetbrains.rider.test.shared.constants.TeamCityTags
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import kotlin.io.path.pathString
import kotlin.reflect.KClass

@Subsystem(SubsystemConstants.GODOT)
@Solution("ResCompletionTest", slnName = "GodotProject.sln")
@TestSettings(sdkVersion = SdkVersion.LATEST_STABLE, buildTool = BuildTool.SDK)
@Tag(TeamCityTags.Plugins.Godot.General)
@Timeout(30)
class GodotRunConfigurationGeneratorTest : PerClassSolutionTestBase() {
    @Test
    @ChecklistItems(["Run configurations/Godot 3 configurations"])
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
    @ChecklistItems(["Run configurations/Godot 3 configurations"])
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
    @ChecklistItems(["Run configurations/Godot 4 configurations"])
    fun `Godot 4 GDScript project creates expected configurations`() {
        assertGeneratedConfigurations(
            GodotVersion.GODOT_4,
            isPureGdScriptProject = true,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, GodotPlayerConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, ExeConfigurationType::class),
            ),
        )
    }

    @Test
    @ChecklistItems(["Run configurations/Godot 4 configurations"])
    fun `Godot 4 CSharp project creates expected configurations`() {
        assertGeneratedConfigurations(
            GodotVersion.GODOT_4,
            isPureGdScriptProject = false,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, GodotPlayerConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotDebugRunCurrentScene.CONFIGURATION_NAME, GodotDebugRunCurrentSceneType::class),
            ),
        )
    }

    @ParameterizedTest
    @EnumSource(GodotVersion::class, names = ["GODOT_3", "GODOT_4"])
    @ChecklistItems(["Run configurations/CMake configurations"])
    fun `CMake project creates expected launch configurations`(godotVersion: GodotVersion) {
        val launchType = if (godotVersion == GodotVersion.GODOT_3) {
            GodotDebugRunConfigurationType::class
        } else {
            DotNetExeConfigurationType::class
        }
        val playerType = if (godotVersion == GodotVersion.GODOT_4) GodotPlayerConfigurationType::class else launchType
        assertGeneratedConfigurations(
            godotVersion,
            isPureGdScriptProject = false,
            expected = buildSet {
                add(GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, playerType))
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
    @ChecklistItems(["Run configurations/CMake configurations"])
    fun `CMake GDScript project creates expected launch configurations`(godotVersion: GodotVersion) {
        val launchType = if (godotVersion == GodotVersion.GODOT_3) {
            GodotDebugRunConfigurationType::class
        } else {
            DotNetExeConfigurationType::class
        }
        val playerType = if (godotVersion == GodotVersion.GODOT_4) GodotPlayerConfigurationType::class else launchType
        assertGeneratedConfigurations(
            godotVersion,
            isPureGdScriptProject = true,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, playerType),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, launchType),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, ExeConfigurationType::class),
            ),
            isCmakeProject = true,
        )
    }

    @Test
    @ChecklistItems(["Run configurations/Unknown Godot version"])
    fun `Unknown version creates no managed configurations`() {
        assertGeneratedConfigurations(GodotVersion.UNKNOWN, isPureGdScriptProject = false, expected = emptySet())
    }

    @Test
    @ChecklistItems(["Run configurations/Generic Godot path"])
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
    @ChecklistItems(["Run configurations/Conflicting Godot paths"])
    fun `Conflicting paths prefer Godot 4`() {
        assertGeneratedConfigurations(
            GodotVersion.BOTH,
            isPureGdScriptProject = false,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, GodotPlayerConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotDebugRunCurrentScene.CONFIGURATION_NAME, GodotDebugRunCurrentSceneType::class),
            ),
        )
        assertGeneratedConfigurations(
            GodotVersion.BOTH,
            isPureGdScriptProject = true,
            expected = setOf(
                GeneratedConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, GodotPlayerConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, DotNetExeConfigurationType::class),
                GeneratedConfiguration(GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME, ExeConfigurationType::class),
            ),
        )
    }

    @Test
    @ChecklistItems(["Run configurations/Conflicting Godot paths"])
    fun `Conflicting paths preserve existing launch configurations`() {
        val runManager = RunManager.getInstance(project)
        runManager.allSettings.toList().forEach(runManager::removeConfiguration)
        val projectType = ProjectType(isPureGdScriptProject = false, isCmakeProject = false)
        generateConfigurations(runManager, GodotVersion.GODOT_3, projectType)
        val existing = runManager.allSettings.filter { it.type is GodotDebugRunConfigurationType }
        runManager.selectedConfiguration = null

        generateConfigurations(runManager, GodotVersion.BOTH, projectType, godotPath = "/other-godot")

        assertEquals(5, runManager.allSettings.size)
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
    @ChecklistItems(["Run configurations/Mono attach configuration cleanup"])
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
    @ChecklistItems(["Run configurations/Saved configurations are preserved"])
    fun `Generation preserves saved attach configurations and selection`(godotVersion: GodotVersion) {
        val runManager = RunManager.getInstance(project)
        for (projectType in listOf(ProjectType(false, false), ProjectType(true, false), ProjectType(false, true))) {
            runManager.allSettings.toList().forEach(runManager::removeConfiguration)
            val monoType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
            val monoAttach = runManager.createConfiguration("Custom Mono attach", monoType.factory)
            val customPort = GODOT_3_DEBUG_PORT + 1
            (monoAttach.configuration as RemoteConfiguration).port = customPort
            val currentSceneType = ConfigurationTypeUtil.findConfigurationType(GodotDebugRunCurrentSceneType::class.java)
            val currentScene = runManager.createConfiguration("Custom current scene", currentSceneType.factory)
            for (settings in listOf(monoAttach, currentScene)) {
                settings.storeInLocalWorkspace()
                runManager.addConfiguration(settings)
            }
            runManager.selectedConfiguration = currentScene

            generateConfigurations(runManager, godotVersion, projectType)

            assertSame(monoAttach, runManager.findConfigurationByName(monoAttach.name))
            assertEquals(customPort, (monoAttach.configuration as RemoteConfiguration).port)
            assertSame(currentScene, runManager.findConfigurationByName(currentScene.name))
            assertSame(currentScene, runManager.selectedConfiguration)
            assertTrue(monoAttach.isStoredInLocalWorkspace)
            assertTrue(currentScene.isStoredInLocalWorkspace)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "false, /godot, /godot",
        "true, /godot, /godot",
        "false, /missing/custom-engine, /new/renamed-engine",
        "true, C:\\missing\\custom-engine.exe, C:\\new\\renamed-engine.exe",
        "false, '', /godot",
        "true, '', /godot",
    )
    fun `Generation migrates the saved Player and preserves its settings`(shared: Boolean, oldPath: String, newPath: String) {
        val runManager = RunManager.getInstance(project)
        runManager.allSettings.toList().forEach(runManager::removeConfiguration)
        val oldType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
        val player = runManager.createConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, oldType.factory)
        val configuration = player.configuration as DotNetExeConfiguration
        configuration.parameters.exePath = oldPath
        configuration.parameters.programParameters = "$PLAYER_PROGRAM_PARAMETERS --verbose"
        configuration.parameters.envs = mapOf("GODOT_TEST" to "value")
        configuration.parameters.runtimeType = DotNetCoreRuntimeType
        configuration.parameters.runtimeArguments = "--custom-runtime-argument"
        configuration.isAllowRunningInParallel = true
        configuration.beforeRunTasks = emptyList()
        player.folderName = "Godot"
        if (shared) player.storeInDotIdeaFolder() else player.storeInLocalWorkspace()
        runManager.addConfiguration(player)
        runManager.selectedConfiguration = player

        repeat(2) {
            generateConfigurations(runManager, GodotVersion.GODOT_4, ProjectType(false, false), godotPath = newPath)

            assertSame(player, runManager.findConfigurationByName(player.name))
            assertSame(player, runManager.selectedConfiguration)
            assertEquals("GodotPlayer", player.type.id)
            assertEquals("GodotPlayer", player.configuration.clone().type.id)
            assertEquals(shared, player.isStoredInDotIdeaFolder)
            assertEquals(!shared, player.isStoredInLocalWorkspace)
            assertEquals("Godot", player.folderName)
            val migrated = player.configuration as DotNetExeConfiguration
            assertEquals(newPath, migrated.parameters.exePath)
            assertEquals("$PLAYER_PROGRAM_PARAMETERS --verbose", migrated.parameters.programParameters)
            assertEquals("value", migrated.parameters.envs["GODOT_TEST"])
            assertEquals(DotNetCoreRuntimeType, migrated.parameters.runtimeType)
            assertEquals("--custom-runtime-argument", migrated.parameters.runtimeArguments)
            assertTrue(migrated.isAllowRunningInParallel)
            assertTrue(migrated.beforeRunTasks.isEmpty())
            assertEquals(1, runManager.allSettings.count { it.type is GodotPlayerConfigurationType })
        }
    }

    @Test
    fun `Generation preserves the selected Player after reload`() {
        val runManager = RunManagerImpl.getInstanceImpl(project)
        runManager.allSettings.toList().forEach(runManager::removeConfiguration)
        val oldType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
        val player = runManager.createConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, oldType.factory)
        (player.configuration as DotNetExeConfiguration).parameters.programParameters = PLAYER_PROGRAM_PARAMETERS
        player.storeInLocalWorkspace()
        runManager.addConfiguration(player)
        runManager.selectedConfiguration = player

        generateConfigurations(runManager, GodotVersion.GODOT_4, ProjectType(false, false))

        val state = runManager.state
        val playerType = ConfigurationTypeUtil.findConfigurationType(GodotPlayerConfigurationType::class.java)
        assertEquals("${playerType.displayName}.${player.name}", state.getAttributeValue("selected"))

        runManager.loadState(state)

        val reloadedPlayer = requireNotNull(runManager.findConfigurationByName(player.name))
        assertEquals(playerType, reloadedPlayer.type)
        assertSame(reloadedPlayer, runManager.selectedConfiguration)
    }

    @ParameterizedTest
    @CsvSource("false, /godot", "true, /godot", "false, /missing/godot", "true, /missing/godot")
    fun `Generation preserves compound references when migrating the Player`(initialized: Boolean, oldPath: String) {
        val runManager = RunManagerImpl.getInstanceImpl(project)
        runManager.allSettings.toList().forEach(runManager::removeConfiguration)
        val oldType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
        val player = runManager.createConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, oldType.factory)
        val oldConfiguration = player.configuration as DotNetExeConfiguration
        oldConfiguration.parameters.exePath = oldPath
        oldConfiguration.parameters.programParameters = PLAYER_PROGRAM_PARAMETERS
        val other = runManager.createConfiguration("Other", oldType.factory)
        for (settings in listOf(player, other)) {
            settings.storeInLocalWorkspace()
            runManager.addConfiguration(settings)
        }

        val compoundType = ConfigurationTypeUtil.findConfigurationType(CompoundRunConfigurationType::class.java)
        val compoundSettings = runManager.createConfiguration("Compound", compoundType) as RunnerAndConfigurationSettingsImpl
        var compound = compoundSettings.configuration as CompoundRunConfiguration
        val target = DefaultExecutionTarget.INSTANCE
        compound.setConfigurationsWithTargets(linkedMapOf(oldConfiguration to target, other.configuration to null))
        if (!initialized) {
            compoundSettings.readExternal(compoundSettings.writeScheme(), false)
            compound = compoundSettings.configuration as CompoundRunConfiguration
        }
        compoundSettings.storeInLocalWorkspace()
        runManager.addConfiguration(compoundSettings)

        repeat(2) {
            generateConfigurations(runManager, GodotVersion.GODOT_4, ProjectType(false, false))

            assertTrue(player.type is GodotPlayerConfigurationType)
            val expected = linkedMapOf(player.configuration to target, other.configuration to null)
            assertEquals(expected, compound.getConfigurationsWithTargets(runManager))

            val reloaded = RunnerAndConfigurationSettingsImpl(runManager)
            reloaded.readExternal(compoundSettings.writeScheme(), false)
            assertEquals(expected, (reloaded.configuration as CompoundRunConfiguration).getConfigurationsWithTargets(runManager))
        }
    }

    @Test
    fun `Generation leaves non-player Godot configurations unchanged`() {
        for (arguments in listOf(EDITOR_PROGRAM_PARAMETERS, "$PLAYER_PROGRAM_PARAMETERS -e", "--version")) {
            assertLegacyPlayerIsPreserved(GODOT_PATH, arguments)
            assertLegacyPlayerIsPreserved("/missing/godot", arguments)
        }
    }

    private fun assertLegacyPlayerIsPreserved(exePath: String, arguments: String) {
        val runManager = RunManager.getInstance(project)
        runManager.allSettings.toList().forEach(runManager::removeConfiguration)
        val oldType = ConfigurationTypeUtil.findConfigurationType(DotNetExeConfigurationType::class.java)
        val player = runManager.createConfiguration(GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME, oldType.factory)
        val configuration = player.configuration as DotNetExeConfiguration
        configuration.parameters.exePath = exePath
        configuration.parameters.programParameters = arguments
        player.storeInLocalWorkspace()
        runManager.addConfiguration(player)
        runManager.selectedConfiguration = player

        generateConfigurations(runManager, GodotVersion.GODOT_4, ProjectType(false, false))

        assertSame(player, runManager.selectedConfiguration)
        assertSame(configuration, player.configuration)
        assertEquals(oldType, player.type)
        assertEquals(exePath, configuration.parameters.exePath)
        assertEquals(arguments, configuration.parameters.programParameters)
        assertEquals(1, runManager.allSettings.count { it.type is GodotPlayerConfigurationType })
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
        if (settings.type is GodotPlayerConfigurationType) {
            assertEquals("GodotPlayer", settings.type.id)
        }
        if (configuration is RemoteConfiguration) {
            assertEquals(GODOT_3_DEBUG_PORT, configuration.port)
            return
        }

        if (settings.type is GodotDebugRunCurrentSceneType) {
            assertEquals(1, configuration.beforeRunTasks.size)
            assertTrue(configuration.beforeRunTasks.single() is BuildSolutionBeforeRunTask)
            val parameters = (configuration as RiderConfigurationParametersAware<*>).parameters
            assertEquals(GODOT_PATH, parameters.exePath)
            assertEquals("", parameters.programParameters)
            assertEquals(PROJECT_PATH, parameters.workingDirectory)
            assertEquals(DotNetCoreRuntimeType, (parameters as DotNetExeConfigurationParameters).runtimeType)
            return
        }

        val parameters = (configuration as RiderConfigurationParametersAware<*>).parameters
        assertEquals(GODOT_PATH, parameters.exePath)
        val expectedProgramParameters = when (settings.name) {
            GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME -> PLAYER_PROGRAM_PARAMETERS
            GodotRunConfigurationGenerator.EDITOR_CONFIGURATION_NAME -> EDITOR_PROGRAM_PARAMETERS
            else -> error("Unexpected configuration: ${settings.name}")
        }
        assertEquals(expectedProgramParameters, parameters.programParameters)
        assertEquals(project.solutionDirectoryPath.pathString, parameters.workingDirectory)

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
