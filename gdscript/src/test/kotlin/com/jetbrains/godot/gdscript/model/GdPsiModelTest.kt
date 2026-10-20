package com.jetbrains.godot.gdscript.model

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import project.psi.util.ProjectAutoloadUtil

@TestApplication
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

        runReadActionBlocking {
            val myClassElement = PsiTreeUtil.findChildrenOfType(psiFile, GdClassNameNmi::class.java).firstOrNull()
            assertNotNull(myClassElement, "$MyClass not found by PSI")

            val myClassSymbol = GdPsiClassSymbolFactory.create(myClassElement!!) as? GdPsiClassSymbol

            assertNotNull(myClassSymbol, "$MyClass symbol not found")

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
            assertNotNull(myVarSymbol, "$MyClass should have '$myVar' property")
            assertTrue(myVarSymbol!!.name == myVar, "$MyClass should have '$myVar' property")
            assertEquals(MyClass, myVarSymbol.declaringClassId)

            val myFunc = "my_func"
            val myFuncSymbol = GdPolySymbolQueriesUtil.getSymbol(
                myClassMemberExecutor, GdPolySymbolKind.METHOD, myFunc, GdPsiMethodSymbol::class.java
            )
            assertNotNull(myFuncSymbol, "$MyClass should have '$myFunc' method")
            assertTrue(myFuncSymbol!!.name == myFunc, "$MyClass should have '$myFunc' method")
            assertEquals(MyClass, myFuncSymbol.declaringClassId)

            assertEquals(Node2D, myClassSymbol.resolveSuperClassSymbol()?.declaringClassName)
        }
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
        runReadActionBlocking {
            val classesElements = PsiTreeUtil.findChildrenOfType(psiFile, GdClassNameNmi::class.java)
            assertTrue(classesElements.isNotEmpty())

            val containingClassElement = classesElements.find { it.name == "ContainingClass" }
            assertNotNull(containingClassElement, "ContainingClass not found by PSI")

            val containingClassSymbol = GdPsiClassSymbolFactory.create(containingClassElement!!) as? GdPsiClassSymbol
            assertNotNull(containingClassSymbol, "ContainingClass class symbol not found")

            val containingClassExecutor = PolySymbolQueryExecutorFactory.createCustom{
                addRootScopes(containingClassSymbol!!.queryScope)
            }

            val outerClassSymbol = GdPolySymbolQueriesUtil.getSymbol(
                containingClassExecutor, GdPolySymbolKind.CLASS, Outer, GdPsiClassSymbol::class.java
            )
            assertNotNull(outerClassSymbol, "$Outer class symbol not found")

            val outerExecutor = PolySymbolQueryExecutorFactory.createCustom{
                addRootScopes(outerClassSymbol!!.queryScope)
            }


            val innerClassSymbol = GdPolySymbolQueriesUtil.getSymbol(
                outerExecutor, GdPolySymbolKind.CLASS, Inner, GdPsiClassSymbol::class.java
            )
            assertNotNull(innerClassSymbol, "$Inner class symbol not found")

            val innerSuperClass = innerClassSymbol!!.resolveSuperClassSymbol()
            assertEquals(MyClass, innerSuperClass?.declaringClassName)

            val innerSuperSuperClass = innerSuperClass?.resolveSuperClassSymbol()
            assertEquals(Node2D, innerSuperSuperClass?.declaringClassName)
        }
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

        runReadActionBlocking {
            val baseFileResourcePath = baseFile.virtualFile.resourcePath()
            val childFileResourcePath = childFile.virtualFile.resourcePath()

            val childSymbol = GdPsiClassSymbolFactory.create(childFile) as? GdPsiResourceClassSymbol
            assertNotNull(childSymbol, "Unnamed child script symbol not found")
            assertTrue(childSymbol is GdPsiResourceClassSymbol, "Expected resource-backed class symbol")
            assertEquals(childFileResourcePath, childSymbol!!.declaringClassName)

            val baseSymbol = childSymbol.resolveSuperClassSymbol()
            assertNotNull(baseSymbol, "Unnamed base script symbol not found")
            assertTrue(baseSymbol is GdPsiResourceClassSymbol, "Expected resource-backed superclass symbol")
            assertEquals(baseFileResourcePath, baseSymbol!!.declaringClassName)
            assertEquals(Node2D, baseSymbol.resolveSuperClassSymbol()?.declaringClassName)

            val childClassMemberExecutor = PolySymbolQueryExecutorFactory.createCustom {
                addRootScopes(childSymbol.queryScope)
            }

            val baseVar = "base_var"
            val inheritedProperty = GdPolySymbolQueriesUtil.getSymbol(
                childClassMemberExecutor, GdPolySymbolKind.PROPERTY, baseVar, GdPsiPropertySymbol::class.java
            )
            assertNotNull(inheritedProperty, "Unnamed child script should inherit '$baseVar'")
            assertEquals(baseVar, inheritedProperty!!.name)

            val baseFunc = "base_func"
            val inheritedMethod = GdPolySymbolQueriesUtil.getSymbol(
                childClassMemberExecutor, GdPolySymbolKind.METHOD, baseFunc, GdPsiMethodSymbol::class.java
            )
            assertNotNull(inheritedMethod, "Unnamed child script should inherit '$baseFunc'")
            assertEquals(baseFunc, inheritedMethod!!.name)
        }
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

        runReadActionBlocking {
            val myClassElement = PsiTreeUtil.findChildrenOfType(psiFile, GdClassNameNmi::class.java).firstOrNull()
            assertNotNull(myClassElement, "$MyClass not found by PSI")

            val myClassSymbol = GdPsiClassSymbolFactory.create(myClassElement!!) as? GdPsiClassSymbol
            assertNotNull(myClassSymbol, "$MyClass symbol not found")

            val myClassMemberExecutor = PolySymbolQueryExecutorFactory.createCustom {
                addRootScopes(myClassSymbol!!.queryScope)
            }

            val myFuncSymbol = GdPolySymbolQueriesUtil.getSymbol(
                myClassMemberExecutor, GdPolySymbolKind.METHOD, "my_func", GdPsiMethodSymbol::class.java
            )
            assertNotNull(myFuncSymbol, "MyClass should have 'my_func' method")
            assertEquals("GDScript method 'my_func(a: int, b: String)'", myFuncSymbol!!.presentation.presentableText)

            val constructorSymbol = GdPolySymbolQueriesUtil.getSymbol(
                myClassMemberExecutor, GdPolySymbolKind.CONSTRUCTOR, "_init", GdPsiConstructorSymbol::class.java
            )
            assertNotNull(constructorSymbol, "MyClass should have a '_init' constructor")
            assertEquals("GDScript constructor '_init(count: int, label: String = \"hi\")'", constructorSymbol!!.presentation.presentableText)
        }
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

        runReadActionBlocking {
            val autoloadSymbol = ProjectAutoloadUtil.listGlobals(project)
                .filter { it.element is GdFile }
                .map { autoload -> GdPsiAutoloadSymbol(autoload.element as GdFile, autoload.key) }
                .firstOrNull {autoload -> autoload.name == "global"}
            assertNotNull(autoloadSymbol, "'global' was not found")
            assertEquals("global", autoloadSymbol!!.name)

            val memberExecutor = PolySymbolQueryExecutorFactory.createCustom {
                addRootScopes(autoloadSymbol.queryScope)
            }

            val scoreProperty = GdPolySymbolQueriesUtil.getSymbol(
                memberExecutor, GdPolySymbolKind.PROPERTY, "score", GdPsiPropertySymbol::class.java
            )
            assertNotNull(scoreProperty, "'score' property should be accessible on 'global' autoload")
            assertEquals("score", scoreProperty!!.name)

            val getScoreMethod = GdPolySymbolQueriesUtil.getSymbol(
                memberExecutor, GdPolySymbolKind.METHOD, "get_score", GdPsiMethodSymbol::class.java
            )
            assertNotNull(getScoreMethod, "'get_score' method should be accessible on 'global' autoload")
        }
    }
}
