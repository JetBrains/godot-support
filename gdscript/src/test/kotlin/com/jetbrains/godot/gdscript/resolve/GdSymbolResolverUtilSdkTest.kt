package com.jetbrains.godot.gdscript.resolve

import com.intellij.model.psi.PsiSymbolReferenceService
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.utils.PolySymbolDelegate.Companion.unwrapAllDelegates
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.psi.GdPsiConstructorSymbol
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReference
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.polySymbols.sdk.GdSdkConstructorSymbol
import gdscript.psi.GdRefIdRef
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GdSymbolResolverUtilSdkTest {
    private val gd: GdSdkFixture by gdSdkFixture("highlighting")

    @Test
    fun testListConstructorSymbolsReturnsAllVector2Overloads() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tpass")
        runReadActionBlocking {
            val vector2 = GdSymbolResolverUtil.resolveCanonicalClassSymbol(gd.project, "Vector2", gd.fixture.file)
            val ctors = GdSymbolResolverUtil.listConstructorSymbols(vector2)

            assertEquals(4, ctors.size)
            assertEquals(listOf(0, 1, 1, 2), ctors.mapNotNull { it.gdSignature?.parameters?.size }.sorted())
        }
    }

    private fun refIdAtCaret(): GdRefIdRef {
        val leaf = gd.fixture.file.findElementAt(gd.fixture.caretOffset)
        return PsiTreeUtil.getParentOfType(leaf, GdRefIdRef::class.java, false)!!
    }

    @Test
    fun testQualifiedNewWithNoArgsResolvesToNoArgConstructorOnly() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tvar v = Vector2.<caret>new()")
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            assertEquals(1, symbols.size)
            val ctor = symbols.single()
            assertEquals(GdPolySymbolKind.CONSTRUCTOR, ctor.kind)
            assertEquals(0, ctor.gdSignature?.parameters?.size)
            assertTrue(ctor.unwrapAllDelegates() is GdSdkConstructorSymbol)
            assertNotNull(refId.resolveSymbolReference())
        }
    }

    @Test
    fun testQualifiedNewWithTwoArgsResolvesToTwoParamConstructorOnly() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tvar v = Vector2.<caret>new(1, 2)")
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            assertEquals(1, symbols.size)
            val ctor = symbols.single()
            assertEquals(GdPolySymbolKind.CONSTRUCTOR, ctor.kind)
            assertEquals(2, ctor.gdSignature?.parameters?.size)
        }
    }

    @Test
    fun testQualifiedNewResolvesToSinglePsiConstructorUnchanged() {
        gd.fixture.configureByText(
            "Test.gd", """
            |class_name Foo
            |func _init(a):
            |	pass
            |
            |func f():
            |	var v = Foo.<caret>new(1)
        """.trimMargin()
        )
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            assertEquals(1, symbols.size)
            assertEquals(GdPolySymbolKind.CONSTRUCTOR, symbols.single().kind)
            assertTrue(symbols.single().unwrapAllDelegates() is GdPsiConstructorSymbol)
        }
    }

    @Test
    fun testQualifiedNewOnPsiClassWithoutExplicitConstructorResolvesToClass() {
        gd.fixture.configureByText(
            "Test.gd", """
            |class_name Hello
            |extends Node
            |
            |func f():
            |	var v = Hello.<caret>new()
        """.trimMargin()
        )
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            assertEquals(1, symbols.size)
            assertEquals(GdPolySymbolKind.CLASS, symbols.single().kind)
        }
    }

    @Test
    fun testQualifiedNewOnSdkClassWithoutExplicitConstructorResolvesToClass() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tvar v = Object.<caret>new()")
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            assertEquals(1, symbols.size)
            assertEquals(GdPolySymbolKind.CLASS, symbols.single().kind)
        }
    }

    @Test
    fun testBareSdkConstructorCallResolvesToClassAndMatchingArityConstructorOnly() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tvar v = <caret>Vector2(1, 2)")
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            assertTrue(symbols.any { it.kind == GdPolySymbolKind.CLASS })
            val ctorSymbols = symbols.filter { it.kind == GdPolySymbolKind.CONSTRUCTOR }
            assertEquals(1, ctorSymbols.size)
            assertEquals(2, ctorSymbols.single().gdSignature?.parameters?.size)
            // Unfiltered resolveSymbolReference() must still return CLASS first - existing callers
            // that don't filter by kind must see unchanged behavior.
            assertEquals(GdPolySymbolKind.CLASS, refId.resolveSymbolReference()?.kind)
        }
    }

    @Test
    fun testBareSdkConstructorCallDoesNotNavigateToClassDeclaration() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tvar v = <caret>Vector2(1, 2)")
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val rawSymbols = PsiSymbolReferenceService.getService().getReferences(refId)
                .flatMap { it.resolveReference() }
                .filterIsInstance<PolySymbol>()

            val classSymbols = rawSymbols.filter { it.unwrapAllDelegates().kind == GdPolySymbolKind.CLASS }
            assertEquals(1, classSymbols.size)
            assertTrue(classSymbols.single().getNavigationTargets(gd.project).isEmpty())

            val ctorSymbols = rawSymbols.filter { it.unwrapAllDelegates().kind == GdPolySymbolKind.CONSTRUCTOR }
            assertEquals(1, ctorSymbols.size)
            assertTrue(ctorSymbols.single().getNavigationTargets(gd.project).isNotEmpty())
        }
    }

    @Test
    fun testBareSdkConstructorCallWithAmbiguousArityKeepsBothSingleArgOverloads() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tvar v = <caret>Vector2(Vector2i(1, 2))")
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            val ctorSymbols = symbols.filter { it.kind == GdPolySymbolKind.CONSTRUCTOR }
            assertEquals(2, ctorSymbols.size)
            assertEquals(listOf(1, 1), ctorSymbols.mapNotNull { it.gdSignature?.parameters?.size }.sorted())
        }
    }

    @Test
    fun testBareClassReferenceWithoutCallResolvesToClassOnly() {
        gd.fixture.configureByText("Test.gd", "func f():\n\tvar v = <caret>Vector2")
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            assertEquals(1, symbols.size)
            assertEquals(GdPolySymbolKind.CLASS, symbols.single().kind)
        }
    }

    @Test
    fun testBarePsiConstructorCallResolvesToClassAndConstructor() {
        gd.fixture.configureByText(
            "Test.gd", """
            |class_name Foo
            |func _init(a):
            |	pass
            |
            |func f():
            |	var v = <caret>Foo(1)
        """.trimMargin()
        )
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            assertTrue(symbols.any { it.kind == GdPolySymbolKind.CLASS })
            assertEquals(1, symbols.count { it.kind == GdPolySymbolKind.CONSTRUCTOR })
        }
    }

    @Test
    fun testBarePsiConstructorCallWithMultipleOverloadsResolvesToMatchingArityOnly() {
        gd.fixture.configureByText(
            "Test.gd", """
            |class_name Foo
            |func _init(a):
            |	pass
            |func _init(a, b):
            |	pass
            |
            |func f():
            |	var v = <caret>Foo(1, 2)
        """.trimMargin()
        )
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            val ctorSymbols = symbols.filter { it.kind == GdPolySymbolKind.CONSTRUCTOR }
            assertEquals(1, ctorSymbols.size)
            assertEquals(2, ctorSymbols.single().gdSignature?.parameters?.size)
        }
    }

    @Test
    fun testBarePsiConstructorCallWithSameArityDisambiguatedBySdkType() {
        gd.fixture.configureByText(
            "Test.gd", """
            |class_name Foo
            |func _init(a: Node):
            |	pass
            |func _init(a: int):
            |	pass
            |
            |func f():
            |	var warmup = Node.new()
            |	var v = <caret>Foo(Node.new())
        """.trimMargin()
        )
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            val ctorSymbols = symbols.filter { it.kind == GdPolySymbolKind.CONSTRUCTOR }
            assertEquals(1, ctorSymbols.size)
            assertEquals("Node", ctorSymbols.single().gdSignature?.parameters?.single()?.type)
        }
    }

    @Test
    fun testBarePsiMethodCallWithMultipleOverloadsResolvesToMatchingArityOnly() {
        gd.fixture.configureByText(
            "Test.gd", """
            |class_name Foo
            |func bar(a):
            |	pass
            |func bar(a, b):
            |	pass
            |
            |func f():
            |	Foo.new().<caret>bar(1, 2)
        """.trimMargin()
        )
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            val methodSymbols = symbols.filter { it.kind == GdPolySymbolKind.METHOD }
            assertEquals(1, methodSymbols.size)
            assertEquals(2, methodSymbols.single().gdSignature?.parameters?.size)
        }
    }

    @Test
    fun testBarePsiMethodCallWithSameAritySdkTypedOverloadsDisambiguated() {
        gd.fixture.configureByText(
            "Test.gd", """
            |class_name Foo
            |func bar(a: Node):
            |	pass
            |func bar(a: int):
            |	pass
            |
            |func f():
            |	var warmup = Node.new()
            |	Foo.new().<caret>bar(Node.new())
        """.trimMargin()
        )
        runReadActionBlocking {
            val refId = refIdAtCaret()
            val symbols = refId.resolveSymbolReferences()

            val methodSymbols = symbols.filter { it.kind == GdPolySymbolKind.METHOD }
            assertEquals(1, methodSymbols.size)
            assertEquals("Node", methodSymbols.single().gdSignature?.parameters?.single()?.type)
        }
    }
}
