package com.jetbrains.godot.gdscript.psi.utils

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.psi.utils.GdExprUtil
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GdExprUtilSdkTest {
    private val gd: GdSdkFixture by gdSdkFixture("highlighting")

    @Test
    fun testTypeAcceptsRejectsUnrelatedSdkTypes() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tpass")
        assertFalse(runReadActionBlocking { GdExprUtil.typeAccepts("int", "Node", gd.project) })
    }

    @Test
    fun testTypeAcceptsAcceptsSdkInheritance() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tpass")
        assertTrue(runReadActionBlocking { GdExprUtil.typeAccepts("Node2D", "Node", gd.project) })
    }

    @Test
    fun testTypeAcceptsAcceptsSdkConstructorConversion() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tpass")
        assertTrue(runReadActionBlocking { GdExprUtil.typeAccepts("String", "int", gd.project) })
    }
}
