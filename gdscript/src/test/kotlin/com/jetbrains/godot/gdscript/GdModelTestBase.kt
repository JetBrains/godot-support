package com.jetbrains.godot.gdscript

import com.intellij.polySymbols.testFramework.HybridTestCase
import com.intellij.polySymbols.testFramework.HybridTestMode
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

abstract class GdModelTestBase : HybridTestCase(HybridTestMode.CodeInsightFixture) {

    protected open val sdkDirectories
        get() = buildList {
            add("sdk/gdextensions")
            add("sdk/4.5.0")
        }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript").pathString
    }

    override fun setUp() {
        super.setUp()

        // copy project.godot to project root
        myFixture.copyFileToProject("project.godot", "project.godot")

        // copy sdk files to project root
        val directories = sdkDirectories.map {
            myFixture.copyDirectoryToProject(it, "")
        }.distinct()

        GdPolySymbolsTestUtils.registerSdk(project, directories.map { it.toNioPath() })
    }
}