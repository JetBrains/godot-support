package com.jetbrains.godot.gdscript

import com.intellij.polySymbols.testFramework.HybridFixtureContext
import com.intellij.testFramework.junit5.fixture.TestFixture
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService
import org.jetbrains.annotations.TestOnly

/**
 * The SDK directories of [gdSdkFixture], relative to the test data directory.
 */
val DEFAULT_SDK_DIRECTORIES: List<String> = listOf("../sdk/gdextensions", "../sdk/4.5.0")

/**
 * Creates a [GdSdkFixture] for the test data directory [testCasePath]. Declare the result as an instance property.
 * [sdkDirectories] are the SDK directories relative to the test data directory.
 */
@TestOnly
fun gdSdkFixture(testCasePath: String, sdkDirectories: List<String> = DEFAULT_SDK_DIRECTORIES): TestFixture<GdSdkFixture> =
    gdFixture { GdSdkFixture(it, testCasePath, sdkDirectories) }

/**
 * A [GdFixture] with the Godot SDK. [setUp] sets the Godot LSP mode to never connect, copies `project.godot`
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
