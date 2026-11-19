package com.jetbrains.rider.plugins.godot.test.cases

import com.intellij.find.usages.api.PsiUsage
import com.intellij.find.usages.api.UsageOptions
import com.intellij.find.usages.impl.AllSearchOptions
import com.intellij.find.usages.impl.buildQuery
import com.intellij.find.usages.impl.searchTargets
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.intellij.psi.search.SearchScope
import com.jetbrains.rider.ideaInterop.find.scopes.RiderSolutionScope
import com.jetbrains.rider.projectView.solutionDirectoryPath
import com.jetbrains.rider.test.OpenSolutionParams
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.TestSettings
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.base.PerTestSettingsTestBase
import com.jetbrains.rider.test.enums.BuildTool
import com.jetbrains.rider.test.enums.sdk.SdkVersion
import com.jetbrains.rider.test.reporting.SubsystemConstants
import com.jetbrains.rider.test.scriptingApi.calculateInBackground
import com.jetbrains.rider.test.scriptingApi.waitPumping
import com.jetbrains.rider.test.scriptingApi.withSolutionFromFolder
import com.jetbrains.rider.test.shared.constants.TeamCityTags
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.time.Duration
import kotlin.io.path.invariantSeparatorsPathString

/**
 * Find Usages on a GDScript `class_name` searches the "Solution" scope by default. That scope must hold every `.gd`
 * usage of the project, in the project root and in a subdirectory, the same as "All Places".
 *
 * `GdScriptOnlyFindUsages` is a GDScript-only project, which Rider opens as a directory.
 */
@Subsystem(SubsystemConstants.GODOT)
@TestSettings(sdkVersion = SdkVersion.LATEST_STABLE, buildTool = BuildTool.SDK)
@Tag(TeamCityTags.Plugins.Godot.General)
@ChecklistItems(["GDScript/Find usages"])
class GdScriptSolutionScopeFindUsagesTest : PerTestSettingsTestBase() {

    @Test
    fun testGdScriptOnlyProject() = withSolutionFromFolder("GdScriptOnlyFindUsages", openParams()) {
        checkClassUsages(project)
    }

    private fun openParams() = OpenSolutionParams { waitForCaches = true }

    /**
     * "All Places" is the reference: it finds both usages once the Godot plugin has indexed the `.gd` files.
     * The "Solution" scope must then find the same usages. The message of a failure lists which usage file the
     * "Solution" scope contains.
     */
    private fun checkClassUsages(project: Project) {
        val root = project.solutionDirectoryPath

        var allPlaces = emptyList<String>()
        val deadline = System.nanoTime() + INDEXING_TIMEOUT.toNanos()
        while (System.nanoTime() < deadline) {
            allPlaces = classUsages(project, root, RiderSolutionScope(project, withExternalItems = true))
            if (allPlaces == EXPECTED_USAGES) break
            waitPumping(Duration.ofSeconds(1))
        }
        assertEquals(EXPECTED_USAGES, allPlaces, "\"All Places\" must find both usages")

        val solutionScope = RiderSolutionScope(project, withExternalItems = false)
        val membership = calculateInBackground(SEARCH_TIMEOUT, "the scope check must finish") {
            runReadAction { EXPECTED_USAGES.associateWith { file(root, it)?.let(solutionScope::contains) } }
        }
        assertEquals(
            EXPECTED_USAGES,
            classUsages(project, root, solutionScope),
            "\"Solution\" must find the same usages as \"All Places\". The \"Solution\" scope contains: $membership",
        )
    }

    /** The files with a usage of `Class`, relative to [root], in the order of their paths. */
    private fun classUsages(project: Project, root: Path, scope: SearchScope): List<String> =
        calculateInBackground(SEARCH_TIMEOUT, "the usage search must finish") {
            runReadAction {
                val classFile = file(root, "class.gd")?.let { PsiManager.getInstance(project).findFile(it) }
                    ?: return@runReadAction emptyList()
                val offset = classFile.text.indexOf(CLASS_NAME_DECLARATION) + "class_name ".length
                val target = searchTargets(classFile, offset).singleOrNull() ?: return@runReadAction emptyList()
                buildQuery(project, target, AllSearchOptions(UsageOptions.createOptions(scope), true))
                    .findAll()
                    .filterIsInstance<PsiUsage>()
                    .filter { !it.declaration }
                    .map { root.relativize(it.file.virtualFile.toNioPath()).invariantSeparatorsPathString }
                    .sorted()
            }
        }

    private fun file(root: Path, relativePath: String) =
        LocalFileSystem.getInstance().findFileByNioFile(root.resolve(relativePath))

    private companion object {
        const val CLASS_NAME_DECLARATION = "class_name Class"
        val EXPECTED_USAGES = listOf("subdirectory/sub_test.gd", "test.gd")
        val INDEXING_TIMEOUT: Duration = Duration.ofSeconds(60)
        val SEARCH_TIMEOUT: Duration = Duration.ofSeconds(30)
    }
}
