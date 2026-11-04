package com.jetbrains.godot.gdscript

import com.intellij.polySymbols.testFramework.HybridFixtureContext
import com.intellij.testFramework.junit5.fixture.TestFixture
import org.jetbrains.annotations.TestOnly

/**
 * The SDK directories of [gdModelFixture], relative to the test data root.
 */
val DEFAULT_MODEL_SDK_DIRECTORIES: List<String> = listOf("sdk/gdextensions", "sdk/4.5.0")

/**
 * Creates a [GdModelFixture]. Declare the result as an instance property.
 * Each test gets a new heavy project with `project.godot` and the SDK files of [sdkDirectories].
 */
@TestOnly
fun gdModelFixture(testCasePath: String, sdkDirectories: List<String> = DEFAULT_MODEL_SDK_DIRECTORIES): TestFixture<GdModelFixture> =
    gdFixture { GdModelFixture(it, testCasePath, sdkDirectories) }

/**
 * The fixture of the GDScript model tests: symbol queries, inheritance and members.
 * [setUp] copies `project.godot` and the [sdkDirectories] to the project root, and registers the SDK files of these directories.
 */
class GdModelFixture(context: HybridFixtureContext, testCasePath: String, private val sdkDirectories: List<String>) :
    GdFixture(context, testCasePath) {

    override fun setUp() {
        super.setUp()

        // copy project.godot to project root
        fixture.copyFileToProject("project.godot", "project.godot")

        // copy sdk files to project root
        val directories = sdkDirectories.map {
            fixture.copyDirectoryToProject(it, "")
        }.distinct()

        GdPolySymbolsTestUtils.registerSdk(project, directories.map { it.toNioPath() }, testRootDisposable)
    }
}