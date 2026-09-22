package com.jetbrains.godot.gdscript.integration

import GdProjectService
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspClientManager
import com.intellij.platform.testFramework.junit5.codeInsight.fixture.codeInsightFixture
import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl
import com.intellij.testFramework.junit5.fixture.TestFixture
import com.intellij.testFramework.junit5.fixture.moduleFixture
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.intellij.testFramework.junit5.fixture.tempPathFixture
import com.intellij.testFramework.junit5.fixture.testFixture
import com.intellij.util.NetworkUtils
import com.jetbrains.godot.test.downloadAndExtractGodot
import com.jetbrains.godot.test.process.GodotEditor
import com.jetbrains.godot.test.process.startGodotEditor
import com.jetbrains.godot.test.project.GodotProject
import com.jetbrains.godot.test.project.GodotSourceBreakpoint
import gdscript.lsp.GodotLspIntegrationProvider
import gdscript.lsp.GodotLspRunningStatusProvider
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService
import gdscript.settings.GdProjectSettingsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.nio.file.Path
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

/** Selects the IDE connection mode for the editor language server. */
enum class GodotEditorLsp {
    ENABLED,
    DISABLED,
}

/** Provides a Godot project, its editor and the files written to the test project. */
class GodotEditorFixture(
    val codeInsight: CodeInsightTestFixture,
    val project: Project,
    val projectDir: Path,
    val lspPort: Int,
    val dapPort: Int,
    val files: Map<String, PsiFile>,
    val breakpoints: Map<String, GodotSourceBreakpoint>,
    /** The directory that holds the gold files of the running test class. */
    val goldDirectory: Path,
    /** The name of the running test, without the `test` prefix and without the `()` suffix. */
    val testName: String,
    private val editor: GodotEditor,
) {
    val editorOutput: String
        get() = editor.output
}

/** Starts a headless Godot editor for a project built by [projectDefinition]. */
fun godotEditorFixture(
    lsp: GodotEditorLsp,
    projectDefinition: () -> GodotProject,
): TestFixture<GodotEditorFixture> {
    val tempDirFixture = tempPathFixture()
    val projectFixture = projectFixture(pathFixture = tempDirFixture, openAfterCreation = true)
    val moduleFixture = projectFixture.moduleFixture(tempDirFixture, addPathToSourceRoot = true)
    val codeInsightFixture = codeInsightFixture(projectFixture, tempDirFixture)

    return testFixture("Godot editor") { context ->
        val ideProject = projectFixture.init()
        moduleFixture.init()
        val codeInsight = codeInsightFixture.init()
        // Both harnesses change the document while the highlighting runs. The LSP reformats, and the
        // evaluate window opens a new code fragment.
        (codeInsight as CodeInsightTestFixtureImpl).canChangeDocumentDuringHighlighting(true)
        val godotProject = projectDefinition()
        val projectDir = tempDirFixture.init()
        val godotExecutable = downloadAndExtractGodot(isMono = false)
        val lspPort = NetworkUtils.findFreePort()
        val dapPort = NetworkUtils.findFreePort(0, setOf(lspPort))

        val files = withContext(Dispatchers.EDT) {
            godotProject.files.mapValuesTo(linkedMapOf()) { (path, text) ->
                codeInsight.addFileToProject(path, text)
            }
        }
        val projectFolder = checkNotNull(files["project.godot"]?.virtualFile?.parent) {
            "The Godot project has no project.godot file"
        }
        GdProjectService.getInstance(ideProject).discoverProject(projectFolder)
        when (lsp) {
            GodotEditorLsp.ENABLED -> {
                GdProjectSettingsState.getInstance(ideProject).state.lspRemoteHostPort = lspPort
                GdLspSettingsFlowService.getInstance(ideProject)
                    .setLspConnectionMode(GdLspConnectionMode.ConnectRunningEditor)
            }
            GodotEditorLsp.DISABLED -> {
                GdLspSettingsFlowService.getInstance(ideProject).setLspConnectionMode(GdLspConnectionMode.Never)
            }
        }
        val godotEditor = startGodotEditor(
            godotExecutable = godotExecutable,
            projectPath = projectDir,
            lspPort = lspPort,
            dapPort = dapPort,
        )
        try {
            godotEditor.awaitLoaded(GODOT_EDITOR_START_TIMEOUT)
            initialized(
                GodotEditorFixture(
                    codeInsight = codeInsight,
                    project = ideProject,
                    projectDir = projectDir,
                    lspPort = lspPort,
                    dapPort = dapPort,
                    files = files,
                    breakpoints = godotProject.breakpoints,
                    goldDirectory = goldDirectory(context.extensionContext.requiredTestClass),
                    testName = context.testName,
                    editor = godotEditor,
                )
            ) {
                var lspFailure: Throwable? = null
                try {
                    stopLsp(ideProject)
                }
                catch (failure: Throwable) {
                    lspFailure = failure
                }
                try {
                    godotEditor.stop(PROCESS_EXIT_TIMEOUT)
                }
                catch (processFailure: Throwable) {
                    if (lspFailure == null) {
                        throw processFailure
                    }
                    lspFailure.addSuppressed(processFailure)
                }
                lspFailure?.let { throw it }
            }
        }
        catch (failure: Throwable) {
            val output = godotEditor.output
            try {
                godotEditor.stop(PROCESS_EXIT_TIMEOUT)
            }
            catch (cleanupFailure: Throwable) {
                failure.addSuppressed(cleanupFailure)
            }
            failure.addSuppressed(IllegalStateException("Godot editor output:\n$output"))
            throw failure
        }
    }
}

/**
 * Returns the gold directory of [testClass] in the source checkout.
 *
 * The files live beside the test resources of the module, so a developer edits them in the working copy. The
 * platform finds the checkout from the class file, and it falls back to the home path that the Bazel test
 * runner sets.
 */
private fun goldDirectory(testClass: Class<*>): Path {
    val home = PathManager.getHomeDirFor(testClass) ?: PathManager.getHomeDir()
    return home.resolve(TEST_DATA_DIRECTORY).resolve(testClass.simpleName)
}

/**
 * Stops the Godot language server for good.
 *
 * The plugin restarts the client from a debounced queue, so a stop alone races with a queued start. The
 * `Never` mode makes that queue stop the client instead of starting it, so the stop holds.
 *
 * The wait runs off the EDT, because the stop needs the EDT itself.
 */
private suspend fun stopLsp(project: Project) {
    GdLspSettingsFlowService.getInstance(project).setLspConnectionMode(GdLspConnectionMode.Never)
    LspClientManager.getInstance(project).stopClients(GodotLspIntegrationProvider::class.java)
    val stopped = withTimeoutOrNull(PROCESS_EXIT_TIMEOUT) {
        while (GodotLspRunningStatusProvider.isLspRunning(project) ||
            GodotLspRunningStatusProvider.isLspStartingUp(project)
        ) {
            delay(LSP_STOP_POLL_INTERVAL)
        }
        true
    }
    check(stopped == true) { "The Godot language server did not stop" }
}

private const val TEST_DATA_DIRECTORY = "dotnet/Plugins/godot-support/gdscript/integrationTest/src/test/testData"
private val GODOT_EDITOR_START_TIMEOUT = 2.minutes
private val PROCESS_EXIT_TIMEOUT = 1.minutes
private val LSP_STOP_POLL_INTERVAL = 100.milliseconds
