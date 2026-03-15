package com.jetbrains.rider.plugins.godot.cpp

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.jetbrains.rider.projectView.solutionDirectoryPath
import kotlin.io.path.exists
import kotlin.io.path.useLines

@Service(Service.Level.PROJECT)
class GodotEngineSolutionAvailability(private val project: Project) {
    private var isAvailable: Boolean? = null

    fun isAvailable(): Boolean {
        if (isAvailable != null) return isAvailable!!
        val result = isPossibleToGenerateSolution()
        isAvailable = result
        return result
    }

    private fun isPossibleToGenerateSolution(): Boolean {
        val folder = project.solutionDirectoryPath
        val sconsFile = folder.resolve("SConstruct")
        if (!sconsFile.exists()) return false

        return sconsFile.useLines { lines ->
            lines.any { it.trimStart().startsWith("opts.Add(BoolVariable(\"vsproj\"") }
        }
    }

    companion object {
        fun getInstance(project: Project): GodotEngineSolutionAvailability = project.getService(GodotEngineSolutionAvailability::class.java)
    }
}