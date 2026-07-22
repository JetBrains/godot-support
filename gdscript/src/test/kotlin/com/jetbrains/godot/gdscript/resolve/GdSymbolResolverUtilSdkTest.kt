package com.jetbrains.godot.gdscript.resolve

import com.intellij.polySymbols.utils.PolySymbolDelegate.Companion.unwrapAllDelegates
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.psi.GdPsiConstructorSymbol
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReference
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.polySymbols.sdk.GdSdkConstructorSymbol
import gdscript.psi.GdRefIdRef
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdSymbolResolverUtilSdkTest : GdTestCaseWithSdk("highlighting") {

    @Test
    fun testListConstructorSymbolsReturnsAllVector2Overloads() {
        myFixture.configureByText("Test.gd", "func f():\n\tpass")
        val vector2 = GdSymbolResolverUtil.resolveCanonicalClassSymbol(project, "Vector2", myFixture.file)
        val ctors = GdSymbolResolverUtil.listConstructorSymbols(vector2)

        assertEquals(4, ctors.size)
        assertEquals(listOf(0, 1, 1, 2), ctors.mapNotNull { it.gdSignature?.parameters?.size }.sorted())
    }

    private fun refIdAtCaret(): GdRefIdRef {
        val leaf = myFixture.file.findElementAt(myFixture.caretOffset)
        return PsiTreeUtil.getParentOfType(leaf, GdRefIdRef::class.java, false)!!
    }

    @Test
    fun testQualifiedNewWithNoArgsResolvesToNoArgConstructorOnly() {
        myFixture.configureByText("Test.gd", "func f():\n\tvar v = Vector2.<caret>new()")
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        assertEquals(1, symbols.size)
        val ctor = symbols.single()
        assertEquals(GdPolySymbolKind.CONSTRUCTOR, ctor.kind)
        assertEquals(0, ctor.gdSignature?.parameters?.size)
        assertTrue(ctor.unwrapAllDelegates() is GdSdkConstructorSymbol)
        assertNotNull(refId.resolveSymbolReference())
    }

    @Test
    fun testQualifiedNewWithTwoArgsResolvesToTwoParamConstructorOnly() {
        myFixture.configureByText("Test.gd", "func f():\n\tvar v = Vector2.<caret>new(1, 2)")
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        assertEquals(1, symbols.size)
        val ctor = symbols.single()
        assertEquals(GdPolySymbolKind.CONSTRUCTOR, ctor.kind)
        assertEquals(2, ctor.gdSignature?.parameters?.size)
    }

    @Test
    fun testQualifiedNewResolvesToSinglePsiConstructorUnchanged() {
        myFixture.configureByText(
            "Test.gd", """
            |class_name Foo
            |func _init(a):
            |	pass
            |
            |func f():
            |	var v = Foo.<caret>new(1)
        """.trimMargin()
        )
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        assertEquals(1, symbols.size)
        assertEquals(GdPolySymbolKind.CONSTRUCTOR, symbols.single().kind)
        assertTrue(symbols.single().unwrapAllDelegates() is GdPsiConstructorSymbol)
    }

    @Test
    fun testBareSdkConstructorCallResolvesToClassAndMatchingArityConstructorOnly() {
        myFixture.configureByText("Test.gd", "func f():\n\tvar v = <caret>Vector2(1, 2)")
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

    @Test
    fun testBareSdkConstructorCallWithAmbiguousArityKeepsBothSingleArgOverloads() {
        myFixture.configureByText("Test.gd", "func f():\n\tvar v = <caret>Vector2(Vector2i(1, 2))")
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        val ctorSymbols = symbols.filter { it.kind == GdPolySymbolKind.CONSTRUCTOR }
        assertEquals(2, ctorSymbols.size)
        assertEquals(listOf(1, 1), ctorSymbols.mapNotNull { it.gdSignature?.parameters?.size }.sorted())
    }

    @Test
    fun testBareClassReferenceWithoutCallResolvesToClassOnly() {
        myFixture.configureByText("Test.gd", "func f():\n\tvar v = <caret>Vector2")
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        assertEquals(1, symbols.size)
        assertEquals(GdPolySymbolKind.CLASS, symbols.single().kind)
    }

    @Test
    fun testBarePsiConstructorCallResolvesToClassAndConstructor() {
        myFixture.configureByText(
            "Test.gd", """
            |class_name Foo
            |func _init(a):
            |	pass
            |
            |func f():
            |	var v = <caret>Foo(1)
        """.trimMargin()
        )
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        assertTrue(symbols.any { it.kind == GdPolySymbolKind.CLASS })
        assertEquals(1, symbols.count { it.kind == GdPolySymbolKind.CONSTRUCTOR })
    }

    @Test
    fun testBarePsiConstructorCallWithMultipleOverloadsResolvesToMatchingArityOnly() {
        myFixture.configureByText(
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
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        val ctorSymbols = symbols.filter { it.kind == GdPolySymbolKind.CONSTRUCTOR }
        assertEquals(1, ctorSymbols.size)
        assertEquals(2, ctorSymbols.single().gdSignature?.parameters?.size)
    }

    @Test
    fun testBarePsiConstructorCallWithSameArityDisambiguatedBySdkType() {
        myFixture.configureByText(
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
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        val ctorSymbols = symbols.filter { it.kind == GdPolySymbolKind.CONSTRUCTOR }
        assertEquals(1, ctorSymbols.size)
        assertEquals("Node", ctorSymbols.single().gdSignature?.parameters?.single()?.type)
    }

    @Test
    fun testBarePsiMethodCallWithMultipleOverloadsResolvesToMatchingArityOnly() {
        myFixture.configureByText(
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
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        val methodSymbols = symbols.filter { it.kind == GdPolySymbolKind.METHOD }
        assertEquals(1, methodSymbols.size)
        assertEquals(2, methodSymbols.single().gdSignature?.parameters?.size)
    }

    @Test
    fun testBarePsiMethodCallWithSameAritySdkTypedOverloadsDisambiguated() {
        myFixture.configureByText(
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
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        val methodSymbols = symbols.filter { it.kind == GdPolySymbolKind.METHOD }
        assertEquals(1, methodSymbols.size)
        assertEquals("Node", methodSymbols.single().gdSignature?.parameters?.single()?.type)
    }
}
