package com.jetbrains.godot.gdscript.parser

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdVarDeclSt
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdDocumentedTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases").pathString
    }

    @Test
    fun testDocumentationCommentsParsing() {
        val file = myFixture.configureByFile("documentation_comments.gd")

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
        val documentedFunc = methods.find { it.name == "documented_func" }
        assertNotNull(documentedFunc)
        assertEquals("This is a brief.", documentedFunc!!.brief())
        assertEquals("This is a brief.\n\nThis is a description.\nIt has multiple lines.\n\n" +
            "@tutorial: Tutorial URL\n@tutorial(Tutorial Name 2): Tutorial URL 2\n" +
            "@deprecated: Use something else.\n@experimental: This is experimental.", documentedFunc.description())

        val tutorials = documentedFunc.tutorials()
        assertEquals(2, tutorials.size)
        assertEquals("Tutorial URL", tutorials[0].name)
        assertEquals("Tutorial URL", tutorials[0].url)
        assertEquals("Tutorial Name 2", tutorials[1].name)
        assertEquals("Tutorial URL 2", tutorials[1].url)

        assertTrue(documentedFunc.isDeprecated())
        assertTrue(documentedFunc.isExperimental())

        // static_func
        val staticFunc = methods.find { it.name == "static_func" }
        assertNotNull(staticFunc)
        assertEquals("This is a static function.", staticFunc!!.brief())
        assertEquals("This is a static function.\n@experimental", staticFunc.description())

        assertFalse(staticFunc.isDeprecated())
        assertTrue(staticFunc.isExperimental())

        // Test class variable documentation
        val class_variables = PsiTreeUtil.findChildrenOfType(file, GdClassVarDeclTl::class.java).toList()
        val xVar = class_variables.find { it.name == "x" }
        assertNotNull(xVar)
        assertEquals("This is a brief for var x.", xVar!!.brief())
        assertEmpty(xVar.tutorials())

        assertFalse(xVar.isDeprecated())
        assertFalse(xVar.isExperimental())

        // Test local variable documentation
        val local_variables = PsiTreeUtil.findChildrenOfType(documentedFunc, GdVarDeclSt::class.java).toList()
        val yVar = local_variables.find { it.name == "y" }
        assertNotNull(yVar)
        assertEmpty(yVar!!.brief())
        assertEmpty(yVar.description())
        assertEmpty(yVar.tutorials())

        assertFalse(yVar.isDeprecated())
        assertFalse(yVar.isExperimental())

        val zVar = local_variables.find { it.name == "z" }
        assertNotNull(zVar)
        assertEquals("This is a brief for var z.", zVar!!.brief())
        assertEquals("This is a brief for var z.\n\nThis is a description for var z.", zVar.description())
        assertEmpty(zVar.tutorials())

        assertFalse(zVar.isDeprecated())
        assertFalse(zVar.isExperimental())
    }
}