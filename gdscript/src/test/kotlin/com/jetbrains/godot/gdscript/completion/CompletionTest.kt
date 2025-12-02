package com.jetbrains.godot.gdscript.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class CompletionTest : BasePlatformTestCase() {

    fun getSdkPath(): Path {
        val path = getBaseTestDataPath().resolve("testData/gdscript/sdk")
        assertTrue(path.exists())
        return path
    }

    fun getProjectGodot():Path{
        val path = getBaseTestDataPath().resolve("testData/gdscript/project.godot")
        assertTrue(path.exists())
        return path
    }

    fun getSdkFiles(): Array<String> {
        val sdkFiles = Files.walk(getSdkPath()) // walking recursively
            .filter { it.isRegularFile() && it.fileName.toString().endsWith(".gd") }
            .map { it.pathString }
            .toList()
            .toTypedArray()
        return sdkFiles
    }

    // todo: for some reason those tests don't work with SDK
    //@Test
    //fun testWithSdk01() {
    //    val code = """
    //        |func a():
    //        |	var previous_mode := Input.<caret>
    //    """.trimMargin()
    //
    //    //myFixture.configureByFiles(*getSdkFiles())
    //    myFixture.configureByFiles(getProjectGodot().pathString)
    //    myFixture.configureByText("MyInputTest.gd", code)
    //
    //    GdLibraryManager.registerSdkIfNeeded(getSdkPath(), myFixture.project)
    //    DumbService.getInstance(project).waitForSmartMode()
    //
    //    val lookups = myFixture.completeBasic()?.map { it.lookupString }?.toSet().orEmpty()
    //
    //    assertTrue("Expected MOUSE_MODE_VISIBLE in completion", lookups.contains("MOUSE_MODE_VISIBLE"))
    //}

    @Test
    fun testWithSdk01() {
        val code = """
            |func a():
            |	var previous_mode := Input.<caret> #anonymous enum in the static class context
        """.trimMargin()

        myFixture.configureByFiles(*getSdkFiles())
        myFixture.configureByText("MyInputTest.gd", code)
        val lookups = myFixture.completeBasic()?.map { it.lookupString }?.toSet().orEmpty()
        assertTrue("Expected MOUSE_MODE_VISIBLE in completion", lookups.contains("MOUSE_MODE_VISIBLE"))
    }

    @Test
    fun testWithSdk02() {
        val code = """
            |func a():
            |	var previous_mode := Input.<caret> # Completion from the Input instance in the _GlobalScope, like action_press
        """.trimMargin()

        myFixture.configureByFiles(*getSdkFiles())
        myFixture.configureByText("MyInputTest.gd", code)

        val lookups = myFixture.completeBasic()?.map { it.lookupString }?.toSet().orEmpty()

        assertTrue("Expected action_press in completion", lookups.contains("action_press"))
    }

    //@Test
    //fun testWithSdk03() {
    //    myFixture.configureByFiles(*getSdkFiles())
    //    val code = """
    //        |func a():
    //        |	var previous_mode := Input.mouse_mode
    //        |	Input.mouse_mode = prev<caret>
    //    """.trimMargin()
    //    myFixture.configureByText("MyInputTest.gd", code)
    //
    //    val lookups = myFixture.completeBasic()?.map { it.lookupString }?.toSet().orEmpty()
    //
    //    assertTrue("Expected previous_mode in completion", lookups.contains("previous_mode"))
    //}

    @Test
    fun testCompletionAfterConstructedInnerClass() {
        val code = """
            |class_name NestedClassErrors
            |
            |func in_the_outer_wrong():
            |    A1.new().<caret>
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
            |            class D1:
            |                func pp():
            |                    pass
        """.trimMargin()

        myFixture.configureByText("NestedClassErrors.gd", code)
        val lookups = myFixture.completeBasic()?.map { it.lookupString }?.toSet().orEmpty()

        // Expectations: only A1's own members are suggested here
        assertTrue("Expected method ppa1 in completion", lookups.contains("ppa1"))
        assertTrue("Expected inner class B1 in completion", lookups.contains("B1"))

        // Must not include self and deeper nested classes of A1 (through B1)
        assertFalse("A1 should not be suggested directly after A1.new()", lookups.contains("A1"))
        assertFalse("C1 should not be suggested directly after C1.new()", lookups.contains("C1"))
        assertFalse("D1 should not be suggested directly after A1.new()", lookups.contains("D1"))
    }

    @Test
    fun testCompletionAfterClass() {
        val code = """
            |class_name NestedClassErrors
            |
            |func in_the_outer_wrong():
            |    A1.<caret>
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
        """.trimMargin()

        myFixture.configureByText("NestedClassErrors.gd", code)
        val lookups = myFixture.completeBasic()?.map { it.lookupString }?.toSet().orEmpty()

        // Expectations: only A1 subtype
        assertTrue("Expected inner class B1 in completion", lookups.contains("B1"))

        assertFalse("Type A1 should not have ppa1 in completion", lookups.contains("ppa1"))
    }

    @Test
    fun testCompletionAfterClass2() {
        val code = """
            |class_name NestedClassErrors
            |
            |func in_the_outer_wrong():
            |    A1.B1.<caret>
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
        """.trimMargin()

        myFixture.configureByText("NestedClassErrors.gd", code)
        val lookups = myFixture.completeBasic()?.map { it.lookupString }?.toSet().orEmpty()

        // Expectations: only A1 subtype
        assertTrue("Expected inner class C1 in completion", lookups.contains("C1"))

        assertFalse("Type A1.B1 should not have A1 in completion", lookups.contains("A1"))
        assertFalse("Type A1.B1 should not have B1 in completion", lookups.contains("B1"))
        assertFalse("Type A1.B1 should not have ppa1 in completion", lookups.contains("ppa1"))
    }
}