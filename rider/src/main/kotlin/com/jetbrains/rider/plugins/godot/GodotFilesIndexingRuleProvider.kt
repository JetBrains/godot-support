package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.jetbrains.rd.util.threading.coroutines.nextNotNullValue
import com.jetbrains.rider.model.godot.frontendBackend.godotFrontendBackendModel
import com.jetbrains.rider.projectView.indexing.files.RiderFilesIndexingRule
import com.jetbrains.rider.projectView.indexing.files.RiderFilesIndexingRuleProvider
import com.jetbrains.rider.projectView.solution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class GodotFilesIndexingRuleProvider : RiderFilesIndexingRuleProvider {
    override suspend fun collectRules(project: Project): List<RiderFilesIndexingRule> {
        val model = project.solution.godotFrontendBackendModel

        // Wait for godot model initialized on backend
        model.godotInitialized.nextNotNullValue()

        // Calculate rules if any
        val descriptor = model.godotDescriptor.valueOrNull ?: return emptyList()
        val projectBaseFile = withContext(Dispatchers.IO) {
            VfsUtil.findFileByIoFile(File(descriptor.mainProjectBasePath), true)
        } ?: return emptyList()

        val rule = RiderFilesIndexingRule {
            it.name.endsWith(".gd", true) && VfsUtil.isAncestor(projectBaseFile, it, true)
        }
        return listOf(rule)
    }
}