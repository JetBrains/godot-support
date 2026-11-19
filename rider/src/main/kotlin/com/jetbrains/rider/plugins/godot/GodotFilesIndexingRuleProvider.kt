package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.isFile
import com.jetbrains.rd.util.threading.coroutines.nextNotNullValueAsync
import com.jetbrains.rider.ijent.extensions.toVirtualFile
import com.jetbrains.rider.model.godot.frontendBackend.godotFrontendBackendModel
import com.jetbrains.rider.projectView.indexing.files.RiderFilesIndexingRule
import com.jetbrains.rider.projectView.indexing.files.RiderFilesIndexingRuleProvider
import com.jetbrains.rider.projectView.isDirectorySolution
import com.jetbrains.rider.projectView.solution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// In Rider, when files are not in the csproj, GDScript and TSCN files may be not indexed
// To ensure features like GoToFile and ScenePreview would work, we make sure .gd and .tscn are indexed

class GodotFilesIndexingRuleProvider : RiderFilesIndexingRuleProvider {
    override suspend fun collectRules(project: Project): List<RiderFilesIndexingRule> {
        // for some obscure reason, adding indexing rules to a virtual solution would exclude them from Solution scope of the GotoFile.
        if (project.isDirectorySolution)
            return emptyList()

        val model = project.solution.godotFrontendBackendModel

        // Wait for godot model initialized on backend
        val lifetime = GodotProjectLifetimeService.getLifetime(project)
        if (!model.isGodotProject.nextNotNullValueAsync(lifetime).await()) return emptyList()

        // Calculate rules if any
        val descriptor = model.godotDescriptor.valueOrNull ?: return emptyList()
        val projectBaseFile = withContext(Dispatchers.IO) {
            descriptor.mainProjectBasePath.toVirtualFile(true)
        } ?: return emptyList()

        val rule = RiderFilesIndexingRule({
            if (isGodotIndexedFile(it) && VfsUtil.isAncestor(projectBaseFile, it, true))
                it
            else
                null
        })
        return listOf(rule)
    }
}

/**
 * Whether [file] is a GDScript or a scene file. [GodotFilesIndexingRuleProvider] indexes these files, and
 * [GodotGotoCustomizer] puts them into the "Solution" scope.
 */
// The scene files: https://youtrack.jetbrains.com/issue/RIDER-131262/Rider-Godots-scene-preview-doesnt-work-in-c.#focus=Comments-27-13239615.0-0
internal fun isGodotIndexedFile(file: VirtualFile): Boolean =
    file.isFile && (file.name.endsWith(".gd", true) || file.name.endsWith(".tscn", true))