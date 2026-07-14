package com.jetbrains.godot.gdscript

import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService

/**
 * Base test case for GdScript PolySymbol feature tests (completion, highlighting, navigation, etc.).
 *
 * For testing GdScript PolySymbol feature with SDK support.
 *
 * @see GdTestCase
 */
abstract class GdTestCaseWithSdk(testCasePath: String) : GdTestCase(testCasePath) {

    protected open val sdkDirectories: List<String>
        get() = buildList {
            add("../sdk/gdextensions")
            add("../sdk/4.5.0")
        }

    override fun setUp() {
        super.setUp()

        // prevent the Godot LSP from connecting
        // if not here, the tests would throw an exception sometimes
        GdLspSettingsFlowService.getInstance(project).setLspConnectionMode(GdLspConnectionMode.Never)

        // copy project.godot to project root
        myFixture.copyFileToProject("../project.godot", "project.godot")

        // copy sdk files to project root
        val directories = sdkDirectories.map {
            myFixture.copyDirectoryToProject(it, "")
        }.distinct()

        GdPolySymbolsTestUtils.registerSdk(project, directories.map { it.toNioPath() })
    }

}
