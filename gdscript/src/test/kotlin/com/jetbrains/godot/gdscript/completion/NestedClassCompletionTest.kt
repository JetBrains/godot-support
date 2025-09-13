package com.jetbrains.godot.gdscript.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class NestedClassCompletionTest : BasePlatformTestCase() {

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
