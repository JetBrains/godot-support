package com.jetbrains.godot.gdscript.psi.utils

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.psi.utils.GdExprUtil
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GdExprUtilSdkTest : GdTestCaseWithSdk("highlighting") {

    @Test
    fun testTypeAcceptsRejectsUnrelatedSdkTypes() {
        myFixture.configureByText("Test.gd", "func f():\n\tpass")
        assertFalse(runReadActionBlocking { GdExprUtil.typeAccepts("int", "Node", project) })
    }

    @Test
    fun testTypeAcceptsAcceptsSdkInheritance() {
        myFixture.configureByText("Test.gd", "func f():\n\tpass")
        assertTrue(runReadActionBlocking { GdExprUtil.typeAccepts("Node2D", "Node", project) })
    }

    @Test
    fun testTypeAcceptsAcceptsSdkConstructorConversion() {
        myFixture.configureByText("Test.gd", "func f():\n\tpass")
        assertTrue(runReadActionBlocking { GdExprUtil.typeAccepts("String", "int", project) })
    }
}
