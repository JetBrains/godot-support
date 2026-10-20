package com.jetbrains.godot.gdscript

import com.intellij.polySymbols.testFramework.HybridFixtureContext
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService

/**
 * Base test case for GdScript PolySymbol feature tests (completion, highlighting, navigation, etc.).
 *
 * For testing GdScript PolySymbol feature with SDK support.
 * [sdkDirectories] are the SDK directories relative to the test data path, which [GdSdkFixture] copies and registers.
 *
 * @see GdTestCase
 */
abstract class GdTestCaseWithSdk(
    testCasePath: String,
    sdkDirectories: List<String> = DEFAULT_SDK_DIRECTORIES,
) : GdTestCase(testCasePath, { context, path -> GdSdkFixture(context, path, sdkDirectories) }) {

    companion object {
        val DEFAULT_SDK_DIRECTORIES: List<String> = listOf("../sdk/gdextensions", "../sdk/4.5.0")
    }
}

/**
 * The state of [GdTestCaseWithSdk]. [setUp] sets the Godot LSP mode to never connect, copies `project.godot`
 * and the [sdkDirectories] to the project root, and registers the SDK files of these directories.
 */
open class GdSdkFixture(
    context: HybridFixtureContext,
    testCasePath: String,
    private val sdkDirectories: List<String>,
) : GdFixture(context, testCasePath) {

    override fun setUp() {
        super.setUp()

        // prevent the Godot LSP from connecting
        // if not here, the tests would throw an exception sometimes
        GdLspSettingsFlowService.getInstance(project).setLspConnectionMode(GdLspConnectionMode.Never)

        // copy project.godot to project root
        fixture.copyFileToProject("../project.godot", "project.godot")

        // copy sdk files to project root
        val directories = sdkDirectories.map {
            fixture.copyDirectoryToProject(it, "")
        }.distinct()

        GdPolySymbolsTestUtils.registerSdk(project, directories.map { it.toNioPath() }, testRootDisposable)
    }

}
