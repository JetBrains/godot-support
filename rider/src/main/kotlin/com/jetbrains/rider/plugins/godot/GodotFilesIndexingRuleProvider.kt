package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.isFile
import com.jetbrains.rd.util.threading.coroutines.nextNotNullValue
import com.jetbrains.rider.model.godot.frontendBackend.godotFrontendBackendModel
import com.jetbrains.rider.projectView.indexing.files.RiderFilesIndexingRule
import com.jetbrains.rider.projectView.indexing.files.RiderFilesIndexingRuleProvider
import com.jetbrains.rider.projectView.isDirectorySolution
import com.jetbrains.rider.projectView.solution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.path.Path

// todo: instead of this, I think gdscript plugin should index everything Godot-releated under the project.godot folder

class GodotFilesIndexingRuleProvider : RiderFilesIndexingRuleProvider {
    override suspend fun collectRules(project: Project): List<RiderFilesIndexingRule> {
        // for some obscure reason, adding indexing rules to a virtual solution would exclude them from Solution scope of the GotoFile.
        if (project.isDirectorySolution)
            return emptyList()

        val model = project.solution.godotFrontendBackendModel

        // Wait for godot model initialized on backend
        if (!model.isGodotProject.nextNotNullValue()) return emptyList()

        // Calculate rules if any
        val descriptor = model.godotDescriptor.valueOrNull ?: return emptyList()
        val projectBaseFile = withContext(Dispatchers.IO) {
            VfsUtil.findFile(Path( descriptor.mainProjectBasePath), true)
        } ?: return emptyList()

        val rule = RiderFilesIndexingRule({
            if (!it.isFile) return@RiderFilesIndexingRule null
            if ((it.name.endsWith(".gd", true)
                    || it.name.endsWith(".tscn", true)) // https://youtrack.jetbrains.com/issue/RIDER-131262/Rider-Godots-scene-preview-doesnt-work-in-c.#focus=Comments-27-13239615.0-0
                && VfsUtil.isAncestor(projectBaseFile, it, true))
                it
            else
                null
        })
        return listOf(rule)
    }
}