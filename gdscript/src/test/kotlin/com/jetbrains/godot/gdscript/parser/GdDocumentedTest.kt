package com.jetbrains.godot.gdscript.parser

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdVarDeclSt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Lexer and parser"])
@TestApplication
class GdDocumentedTest {
    private val gd: GdFixture by gdLightFixture("gdscript/parser/godotTestCases")

    @Test
    fun testDocumentationCommentsParsing() {
        val file = gd.fixture.configureByFile("documentation_comments.gd")

        // Test class documentation
        val classNaming = PsiTreeUtil.findChildOfType(file, GdClassNaming::class.java)
        assertNotNull(classNaming)
        assertEquals("Class docstring.\nSecond line of class docstring.", classNaming!!.brief())
        assertEquals("Class docstring.\nSecond line of class docstring.", classNaming.description())

        assertFalse(classNaming.isDeprecated())
        assertFalse(classNaming.isExperimental())

        // Test method documentation
        // documented_func
        val methods = PsiTreeUtil.findChildrenOfType(file, GdMethodDeclTl::class.java).toList()
        val documentedFunc = methods.find { it.getName() == "documented_func" }
        assertNotNull(documentedFunc)
        assertEquals("\tThis is a brief.", documentedFunc!!.brief())
        assertEquals("\tThis is a brief.\n\n\tThis is a description.\n\tIt has multiple lines.\n\n" +
            "\t@tutorial: Tutorial URL\n\t@tutorial(Tutorial Name 2): Tutorial URL 2\n" +
            "\t@deprecated: Use something else.\n\t@experimental: This is experimental.", documentedFunc.description())

        val tutorials = documentedFunc.tutorials()
        assertEquals(2, tutorials.size)
        assertEquals("Tutorial URL", tutorials[0].name)
        assertEquals("Tutorial URL", tutorials[0].url)
        assertEquals("Tutorial Name 2", tutorials[1].name)
        assertEquals("Tutorial URL 2", tutorials[1].url)

        assertTrue(documentedFunc.isDeprecated())
        assertTrue(documentedFunc.isExperimental())

        // static_func
        val staticFunc = methods.find { it.getName() == "static_func" }
        assertNotNull(staticFunc)
        assertEquals("This is a static function.", staticFunc!!.brief())
        assertEquals("This is a static function.\n@experimental", staticFunc.description())

        assertFalse(staticFunc.isDeprecated())
        assertTrue(staticFunc.isExperimental())

        // Test class variable documentation
        val class_variables = PsiTreeUtil.findChildrenOfType(file, GdClassVarDeclTl::class.java).toList()
        val xVar = class_variables.find { it.getName() == "x" }
        assertNotNull(xVar)
        assertEquals("This is a brief for var x.", xVar!!.brief())
        assertTrue(xVar.tutorials().isEmpty())

        assertFalse(xVar.isDeprecated())
        assertFalse(xVar.isExperimental())

        // Test local variable documentation
        val local_variables = PsiTreeUtil.findChildrenOfType(documentedFunc, GdVarDeclSt::class.java).toList()
        val yVar = local_variables.find { it.getName() == "y" }
        assertNotNull(yVar)
        assertTrue(yVar!!.brief().isEmpty())
        assertTrue(yVar.description().isEmpty())
        assertTrue(yVar.tutorials().isEmpty())

        assertFalse(yVar.isDeprecated())
        assertFalse(yVar.isExperimental())

        val zVar = local_variables.find { it.getName() == "z" }
        assertNotNull(zVar)
        assertEquals("This is a brief for var z.", zVar!!.brief())
        assertEquals("This is a brief for var z.\n\nThis is a description for var z.", zVar.description())
        assertTrue(zVar.tutorials().isEmpty())

        assertFalse(zVar.isDeprecated())
        assertFalse(zVar.isExperimental())
    }

    /** The doc comment is above the annotations of a declaration. */
    @Test
    fun testDocumentationCommentAboveAnnotations() {
        val file = gd.fixture.configureByText("annotated.gd", """
            extends Node

            ## Health doc.
            var health: int = 100

            ## Speed doc.
            @export_range(0, 10) var speed: int = 5

            ## Own line doc.
            @export_range(0, 10)
            var speed_own_line: int = 5

            ## Plain export doc.
            @export var plain_export: int = 1

            ## Two annotations doc.
            @export
            @onready var two_annotations: int = 1
        """.trimIndent())

        val variables = PsiTreeUtil.findChildrenOfType(file, GdClassVarDeclTl::class.java).associateBy { it.getName() }
        assertEquals("Health doc.", variables["health"]!!.description())
        assertEquals("Speed doc.", variables["speed"]!!.description())
        assertEquals("Own line doc.", variables["speed_own_line"]!!.description())
        assertEquals("Plain export doc.", variables["plain_export"]!!.description())
        assertEquals("Two annotations doc.", variables["two_annotations"]!!.description())
    }
}
