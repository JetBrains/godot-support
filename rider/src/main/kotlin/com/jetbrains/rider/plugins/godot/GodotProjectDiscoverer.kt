package com.jetbrains.rider.plugins.godot

import com.intellij.execution.RunManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.io.exists
import com.jetbrains.rd.ide.model.RdExistingSolution
import com.jetbrains.rd.platform.util.idea.LifetimedProjectService
import com.jetbrains.rd.util.reactive.IProperty
import com.jetbrains.rd.util.reactive.Property
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.projectView.solutionDescription
import com.jetbrains.rider.projectView.solutionFile
import com.jetbrains.rider.util.idea.getService
import java.io.File
import java.nio.file.Paths

class GodotProjectDiscoverer(project: Project) : LifetimedProjectService(project) {
    private val projectGodotPath = Paths.get(project.basePath!!).resolve("project.godot")
    val isGodotProject: IProperty<Boolean> = Property(false)
    val isGodotUnitTesting: IProperty<Boolean> = Property(false)
    val godotPath : IProperty<String?> = Property(null)

    init {
        val isGodot = getIsGodotProject()
        if (isGodot) {
            if (projectGodotPath.toFile().readLines()
                    .any { it.startsWith("enabled=PoolStringArray") && it.contains("WAT") })
                        isGodotUnitTesting.set(Paths.get(project.basePath!!).resolve("addons/WAT/gui.tscn").exists())

            isGodotProject.set(isGodot)
            val path = getGodotPathFromPlayerRunConfiguration(project) ?: GodotServer.getGodotPath(project)
            godotPath.set(path)
        }
    }

    private fun getGodotPathFromPlayerRunConfiguration(project: Project):String? {
        val runManager = RunManager.getInstance(project)
        val playerSettings = runManager.allSettings.firstOrNull { it.type is GodotDebugRunConfigurationType && it.name == GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME }
        if (playerSettings != null) {
            val config = playerSettings.configuration as GodotDebugRunConfiguration
            val path = config.parameters.exePath
            if (path.length > 0 && File(path).exists()) {
                return path
            }
        }
        return null
    }

    // It's a Godot project, but not necessarily loaded correctly (e.g. it might be opened as folder)
    private fun getIsGodotProject() : Boolean
    {
        return projectGodotPath.exists() && isCorrectlyLoadedSolution(project)
    }

    val port: Int = 23685 // default value, //todo: read custom value from project.godot file

    // Returns false when opening a project as a plain folder
    private fun isCorrectlyLoadedSolution(project: Project): Boolean {
        val solutionFile = project.solutionFile
        return project.solutionDescription is RdExistingSolution && solutionFile.isFile && solutionFile.extension.equals("sln", true)
    }

    companion object {
        fun getInstance(project: Project) = project.getService<GodotProjectDiscoverer>()
        private val logger = Logger.getInstance(GodotProjectDiscoverer::class.java)
    }
}

val Project.projectDir: VirtualFile
    get() = LocalFileSystem.getInstance().findFileByPath(this.basePath!!)!!