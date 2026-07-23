package com.jetbrains.godot.gdscript.folding

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

/** Folding tests for [gdscript.formatter.GdFoldingBuilder]. */
@RunWith(JUnit4::class)
class GdFoldingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String =
        getBaseTestDataPath().resolve("testData/gdscript").pathString

    private fun doFoldingTest() {
        myFixture.testFoldingWithCollapseStatus("$testDataPath/folding/${getTestName(true)}.gd")
    }

    @Test
    fun testRegion() = doFoldingTest()

    @Test
    fun testNestedRegion() = doFoldingTest()

    // Godot recognizes only `#region` with no space; `# region` must not fold.
    @Test
    fun testRegionWithSpaceNotFolded() = doFoldingTest()

    @Test
    fun testTrait() = doFoldingTest()

    @Test
    fun testMultilineString() = doFoldingTest()

    @Test
    fun testSingleLineStringNotFolded() = doFoldingTest()

    @Test
    fun testDict() = doFoldingTest()

    @Test
    fun testArray() = doFoldingTest()

    @Test
    fun testSuiteAndClass() = doFoldingTest()
}
