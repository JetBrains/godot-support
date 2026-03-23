package com.jetbrains.godot.gdscript.polySymbols

import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService

/**
 * Base test case for GdScript PolySymbol feature tests (completion, highlighting, navigation, etc.).
 *
 * For testing GdScript PolySymbol feature with SDK support.
 *
 * @see GdPolySymbolsTestCase
 */
abstract class GdPolySymbolsTestCaseWithSdk(testCasePath: String) : GdPolySymbolsTestCase(testCasePath) {

    protected open val sdkDirectories: List<String>
        get() = buildList {
            add("../sdk/gdextensions")
            add("../sdk/4.5.0")
        }

    /**
     * Path to the project.godot file relative to the path from getTestDataPath()
     */
    protected open val relativeGodotProjectPath: String = "../project.godot"

    override fun setUp() {
        super.setUp()

        // prevent the Godot LSP from connecting
        // if not here, the tests would throw an exception sometimes
        GdLspSettingsFlowService.getInstance(project).setLspConnectionMode(GdLspConnectionMode.Never)

        // copy project.godot to project root
        myFixture.copyFileToProject(relativeGodotProjectPath, "project.godot")

        // copy sdk files to project root
        val directories = sdkDirectories.map {
            myFixture.copyDirectoryToProject(it, "")
        }.distinct()

        GdPolySymbolsTestUtils.registerSdk(project, directories.map { it.toNioPath() })
    }

}
