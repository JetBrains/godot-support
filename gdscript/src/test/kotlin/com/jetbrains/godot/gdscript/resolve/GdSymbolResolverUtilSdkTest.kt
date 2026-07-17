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
    fun testQualifiedNewResolvesToAllSdkConstructorOverloads() {
        myFixture.configureByText("Test.gd", "func f():\n\tvar v = Vector2.<caret>new()")
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        assertEquals(4, symbols.size)
        assertTrue(symbols.all { it.kind == GdPolySymbolKind.CONSTRUCTOR })
        assertTrue(symbols.all { it.unwrapAllDelegates() is GdSdkConstructorSymbol })
        assertNotNull(refId.resolveSymbolReference())
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
    fun testBareSdkConstructorCallResolvesToClassAndAllConstructorOverloads() {
        myFixture.configureByText("Test.gd", "func f():\n\tvar v = <caret>Vector2(1, 2)")
        val refId = refIdAtCaret()
        val symbols = refId.resolveSymbolReferences()

        assertTrue(symbols.any { it.kind == GdPolySymbolKind.CLASS })
        assertEquals(4, symbols.count { it.kind == GdPolySymbolKind.CONSTRUCTOR })
        // Unfiltered resolveSymbolReference() must still return CLASS first - existing callers
        // that don't filter by kind must see unchanged behavior.
        assertEquals(GdPolySymbolKind.CLASS, refId.resolveSymbolReference()?.kind)
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
}
