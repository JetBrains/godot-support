package com.jetbrains.godot.gdscript.model

import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.gdscript.GdModelTestBase
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.psi.GdPsiAutoloadSymbol
import gdscript.polySymbols.psi.GdPsiClassSymbol
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.polySymbols.psi.GdPsiConstructorSymbol
import gdscript.polySymbols.psi.GdPsiMethodSymbol
import gdscript.polySymbols.psi.GdPsiPropertySymbol
import gdscript.polySymbols.psi.GdPsiResourceClassSymbol
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdFile
import gdscript.utils.VirtualFileUtil.resourcePath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import project.psi.util.ProjectAutoloadUtil

@RunWith(JUnit4::class)
class GdPsiModelTest : GdModelTestBase() {

    companion object {
        const val MyClass = "MyClass"
        const val Node2D = "Node2D"
        const val Outer = "Outer"
        const val Inner = "Inner"
    }

    @Test
    fun testPsiSymbolsModel() {
        val psiFile = myFixture.addFileToProject("test.gd", """
            extends Node2D
            class_name MyClass
            
            var my_var = 1
            func my_func():
                pass
        """.trimIndent())

        val myClassElement = PsiTreeUtil.findChildrenOfType(psiFile, GdClassNameNmi::class.java).firstOrNull()
        assertNotNull("$MyClass not found by PSI", myClassElement)

        val myClassSymbol = GdPsiClassSymbolFactory.create(myClassElement!!) as? GdPsiClassSymbol

        assertNotNull("$MyClass symbol not found", myClassSymbol)

        assertEquals(MyClass, myClassSymbol!!.name)
        assertEquals(MyClass, myClassSymbol.classId)
        assertEquals(MyClass, myClassSymbol.declaringClassId)

        val myVar = "my_var"
        val myClassMemberExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScopes(myClassSymbol.queryScope)
        }
        val myVarSymbol = GdPolySymbolQueriesUtil.getSymbol(
            myClassMemberExecutor, GdPolySymbolKind.PROPERTY, myVar, GdPsiPropertySymbol::class.java
        )
        assertNotNull("$MyClass should have '$myVar' property", myVarSymbol)
        assertTrue("$MyClass should have '$myVar' property", myVarSymbol!!.name == myVar)
        assertEquals(MyClass, myVarSymbol.declaringClassId)

        val myFunc = "my_func"
        val myFuncSymbol = GdPolySymbolQueriesUtil.getSymbol(
            myClassMemberExecutor, GdPolySymbolKind.METHOD, myFunc, GdPsiMethodSymbol::class.java
        )
        assertNotNull("$MyClass should have '$myFunc' method", myFuncSymbol)
        assertTrue("$MyClass should have '$myFunc' method", myFuncSymbol!!.name == myFunc)
        assertEquals(MyClass, myFuncSymbol.declaringClassId)

        assertEquals(Node2D, myClassSymbol.resolveSuperClassSymbol()?.declaringClassName)
    }

    @Test
    fun testPsiInnerClassSymbolResolvesSuperClass() {
        val psiFile = myFixture.addFileToProject("test.gd", """
            class_name ContainingClass
            
            class MyClass extends Node2D:
                pass
            
            class Outer:
                class Inner extends MyClass:
                    pass
        """.trimIndent())
        val classesElements = PsiTreeUtil.findChildrenOfType(psiFile, GdClassNameNmi::class.java)
        assertNotEmpty(classesElements)

        val containingClassElement = classesElements.find { it.name == "ContainingClass" }
        assertNotNull("ContainingClass not found by PSI", containingClassElement)

        val containingClassSymbol = GdPsiClassSymbolFactory.create(containingClassElement!!) as? GdPsiClassSymbol
        assertNotNull("ContainingClass class symbol not found", containingClassSymbol)

        val containingClassExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScopes(containingClassSymbol!!.queryScope)
        }

        val outerClassSymbol = GdPolySymbolQueriesUtil.getSymbol(
            containingClassExecutor, GdPolySymbolKind.CLASS, Outer, GdPsiClassSymbol::class.java
        )
        assertNotNull("$Outer class symbol not found", outerClassSymbol)

        val outerExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScopes(outerClassSymbol!!.queryScope)
        }


        val innerClassSymbol = GdPolySymbolQueriesUtil.getSymbol(
            outerExecutor, GdPolySymbolKind.CLASS, Inner, GdPsiClassSymbol::class.java
        )
        assertNotNull("$Inner class symbol not found", innerClassSymbol)

        val innerSuperClass = innerClassSymbol!!.resolveSuperClassSymbol()
        assertEquals(MyClass, innerSuperClass?.declaringClassName)

        val innerSuperSuperClass = innerSuperClass?.resolveSuperClassSymbol()
        assertEquals(Node2D, innerSuperSuperClass?.declaringClassName)
    }

    @Test
    fun testPsiResourceClassSymbolResolvesUnnamedScriptInheritance() {
        val baseFile = myFixture.addFileToProject("base.gd", """
            extends Node2D

            var base_var = 1
            func base_func():
                pass
        """.trimIndent())
        val childFile = myFixture.addFileToProject("child.gd", """
            extends "res://base.gd"

            var child_var = 1
        """.trimIndent()) as GdFile

        val baseFileResourcePath = baseFile.virtualFile.resourcePath()
        val childFileResourcePath = childFile.virtualFile.resourcePath()

        val childSymbol = GdPsiClassSymbolFactory.create(childFile) as? GdPsiResourceClassSymbol
        assertNotNull("Unnamed child script symbol not found", childSymbol)
        assertTrue("Expected resource-backed class symbol", childSymbol is GdPsiResourceClassSymbol)
        assertEquals(childFileResourcePath, childSymbol!!.declaringClassName)

        val baseSymbol = childSymbol.resolveSuperClassSymbol()
        assertNotNull("Unnamed base script symbol not found", baseSymbol)
        assertTrue("Expected resource-backed superclass symbol", baseSymbol is GdPsiResourceClassSymbol)
        assertEquals(baseFileResourcePath, baseSymbol!!.declaringClassName)
        assertEquals(Node2D, baseSymbol.resolveSuperClassSymbol()?.declaringClassName)

        val childClassMemberExecutor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(childSymbol.queryScope)
        }

        val baseVar = "base_var"
        val inheritedProperty = GdPolySymbolQueriesUtil.getSymbol(
            childClassMemberExecutor, GdPolySymbolKind.PROPERTY, baseVar, GdPsiPropertySymbol::class.java
        )
        assertNotNull("Unnamed child script should inherit '$baseVar'", inheritedProperty)
        assertEquals(baseVar, inheritedProperty!!.name)

        val baseFunc = "base_func"
        val inheritedMethod = GdPolySymbolQueriesUtil.getSymbol(
            childClassMemberExecutor, GdPolySymbolKind.METHOD, baseFunc, GdPsiMethodSymbol::class.java
        )
        assertNotNull("Unnamed child script should inherit '$baseFunc'", inheritedMethod)
        assertEquals(baseFunc, inheritedMethod!!.name)
    }

    @Test
    fun testMethodAndConstructorPresentationIncludesParameters() {
        val psiFile = myFixture.addFileToProject("test.gd", """
            class_name $MyClass

            func _init(count: int, label: String = "hi"):
                pass

            func my_func(a: int, b: String) -> void:
                pass
        """.trimIndent())

        val myClassElement = PsiTreeUtil.findChildrenOfType(psiFile, GdClassNameNmi::class.java).firstOrNull()
        assertNotNull("$MyClass not found by PSI", myClassElement)

        val myClassSymbol = GdPsiClassSymbolFactory.create(myClassElement!!) as? GdPsiClassSymbol
        assertNotNull("$MyClass symbol not found", myClassSymbol)

        val myClassMemberExecutor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(myClassSymbol!!.queryScope)
        }

        val myFuncSymbol = GdPolySymbolQueriesUtil.getSymbol(
            myClassMemberExecutor, GdPolySymbolKind.METHOD, "my_func", GdPsiMethodSymbol::class.java
        )
        assertNotNull("MyClass should have 'my_func' method", myFuncSymbol)
        assertEquals(
            "GDScript method 'my_func(a: int, b: String)'",
            myFuncSymbol!!.presentation.presentableText
        )

        val constructorSymbol = GdPolySymbolQueriesUtil.getSymbol(
            myClassMemberExecutor, GdPolySymbolKind.CONSTRUCTOR, "_init", GdPsiConstructorSymbol::class.java
        )
        assertNotNull("MyClass should have a '_init' constructor", constructorSymbol)
        assertEquals(
            "GDScript constructor '_init(count: int, label: String = \"hi\")'",
            constructorSymbol!!.presentation.presentableText
        )
    }

    @Test
    fun testAutoloadSymbolModel() {
        // project.godot registers global.gd as an autoload
        myFixture.addFileToProject("global.gd", """
            extends Node
            var score = 0
            func get_score() -> int:
                return score
        """.trimIndent())

        myFixture.addFileToProject("test.gd", "")

        val autoloadSymbol = ProjectAutoloadUtil.listGlobals(project)
            .filter { it.element is GdFile }
            .map { autoload -> GdPsiAutoloadSymbol(autoload.element as GdFile, autoload.key) }
            .firstOrNull {autoload -> autoload.name == "global"}
        assertNotNull("'global' was not found", autoloadSymbol)
        assertEquals("global", autoloadSymbol!!.name)

        val memberExecutor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(autoloadSymbol.queryScope)
        }

        val scoreProperty = GdPolySymbolQueriesUtil.getSymbol(
            memberExecutor, GdPolySymbolKind.PROPERTY, "score", GdPsiPropertySymbol::class.java
        )
        assertNotNull("'score' property should be accessible on 'global' autoload", scoreProperty)
        assertEquals("score", scoreProperty!!.name)

        val getScoreMethod = GdPolySymbolQueriesUtil.getSymbol(
            memberExecutor, GdPolySymbolKind.METHOD, "get_score", GdPsiMethodSymbol::class.java
        )
        assertNotNull("'get_score' method should be accessible on 'global' autoload", getScoreMethod)
    }
}
