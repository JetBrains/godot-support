package com.jetbrains.godot.gdscript

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.testFramework.HybridFixture
import com.intellij.polySymbols.testFramework.HybridFixtureContext
import com.intellij.polySymbols.testFramework.HybridTestMode
import com.intellij.polySymbols.testFramework.hybridFixture
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import java.nio.file.Path
import kotlin.io.path.pathString

/**
 * The base of the GDScript model tests. Each test gets a new heavy project with `project.godot` and the SDK files of [sdkDirectories].
 */
abstract class GdModelTestBase(sdkDirectories: List<String> = DEFAULT_SDK_DIRECTORIES) {

    protected val gd: GdModelFixture by hybridFixture({ HybridTestMode.CodeInsightFixture }) { GdModelFixture(it, sdkDirectories) }

    protected val myFixture: CodeInsightTestFixture get() = gd.fixture

    protected val project: Project get() = gd.project

    companion object {
        val DEFAULT_SDK_DIRECTORIES: List<String> = listOf("sdk/gdextensions", "sdk/4.5.0")
    }
}

/**
 * The state of [GdModelTestBase]. [setUp] copies `project.godot` and the [sdkDirectories] to the project root,
 * and registers the SDK files of these directories.
 */
class GdModelFixture(context: HybridFixtureContext, private val sdkDirectories: List<String>) :
    HybridFixture(context, getGdTestDataPath(context.testClass)) {

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

internal fun getGdTestDataPath(testClass: Class<*>): String {
    val home = PathManager.getHomeDirFor(testClass)
    val base: Path = home?.resolve("dotnet/Plugins/godot-support/gdscript/src/test")
                     ?: PathManager.getPluginsDir().parent.parent.parent.parent.parent.resolve("src/test")
    return base.resolve("testData/gdscript").pathString
}