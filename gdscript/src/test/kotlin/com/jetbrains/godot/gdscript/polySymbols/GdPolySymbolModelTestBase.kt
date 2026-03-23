package com.jetbrains.godot.gdscript.polySymbols

import com.intellij.polySymbols.testFramework.HybridTestCase
import com.intellij.polySymbols.testFramework.HybridTestMode
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

abstract class GdPolySymbolModelTestBase : HybridTestCase(HybridTestMode.CodeInsightFixture) {

    protected open val sdkDirectories
        get() = buildList {
            add("sdk/gdextensions")
            add("sdk/4.5.0")
        }

    /**
     * Path to the project.godot file relative to the path from getTestDataPath()
     */
    protected open val relativeGodotProjectPath: String = "../project.godot"

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/polySymbols").pathString
    }

    override fun setUp() {
        super.setUp()

        // copy project.godot to project root
        myFixture.copyFileToProject(relativeGodotProjectPath, "project.godot")

        // copy sdk files to project root
        val directories = sdkDirectories.map {
            myFixture.copyDirectoryToProject(it, "")
        }.distinct()

        GdPolySymbolsTestUtils.registerSdk(project, directories.map { it.toNioPath() })
    }
}