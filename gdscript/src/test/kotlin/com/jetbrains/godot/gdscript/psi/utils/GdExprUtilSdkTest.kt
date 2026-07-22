package com.jetbrains.godot.gdscript.psi.utils

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.psi.utils.GdExprUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdExprUtilSdkTest : GdTestCaseWithSdk("highlighting") {

    @Test
    fun testTypeAcceptsRejectsUnrelatedSdkTypes() {
        myFixture.configureByText("Test.gd", "func f():\n\tpass")
        assertFalse(GdExprUtil.typeAccepts("int", "Node", project))
    }

    @Test
    fun testTypeAcceptsAcceptsSdkInheritance() {
        myFixture.configureByText("Test.gd", "func f():\n\tpass")
        assertTrue(GdExprUtil.typeAccepts("Node2D", "Node", project))
    }

    @Test
    fun testTypeAcceptsAcceptsSdkConstructorConversion() {
        myFixture.configureByText("Test.gd", "func f():\n\tpass")
        assertTrue(GdExprUtil.typeAccepts("String", "int", project))
    }
}
