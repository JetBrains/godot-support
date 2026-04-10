package com.jetbrains.godot.gdscript.resolve

import gdscript.psi.GdConstDeclTl
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ResolveInSingleFileTest : ResolveTestBase() {
    @Test
    fun testUnresolvableMembersOnNestedClasses() {
        val file = loadByTestName()
        val annotated = dumpResolvesWithInlineMarkers(file)
        assertGold(annotated)
    }

    @Test
    @Ignore("RIDER-131644 Resolve `super` base class, defined in the same file")
    fun testResolveSuperClass(){
        val file = loadByTestName()
        val annotated = dumpResolvesWithInlineMarkers(file)
        assertGold(annotated)
    }

    @Test
    @Ignore("RIDER-131639 resolve enum members from a separate class")
    fun testGdDictionary(){
        val file = loadByTestName()
        val annotated = dumpResolvesWithInlineMarkers(file)
        assertGold(annotated)
    }

    @Test
    fun testEnum1(){
        val file = loadByTestName()
        val annotated = dumpResolvesWithInlineMarkers(file)
        assertGold(annotated)
    }

    @Test
    @Ignore
    fun testUnnamedEnumsOuterConflicts() {
        val file = loadByTestName()
        val annotated = dumpResolvesWithInlineMarkers(file)
        assertGold(annotated)
    }

    @Test
    @Ignore("RIDER-132087")
    fun testExtendsClass(){
        val file = loadByTestName()
        val annotated = dumpResolvesWithInlineMarkers(file)
        assertGold(annotated)
    }

    @Test
    fun testSelfReferentialConstNoStackOverflow() {
        val file = loadByTestName()
        // RIDER-137850 Calling returnType triggers the infinite type-inference chain without the fix
        file.children.filterIsInstance<GdConstDeclTl>().forEach { it.returnType }
    }
}
