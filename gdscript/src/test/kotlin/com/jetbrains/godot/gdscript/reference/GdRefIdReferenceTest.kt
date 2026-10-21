package com.jetbrains.godot.gdscript.reference

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.polySymbols.psi.GdPsiClassSymbol
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.polySymbols.sdk.GdSdkPropertySymbol
import org.junit.jupiter.api.Test

@TestApplication
class GdRefIdReferenceTest {
    private val gd: GdSdkFixture by gdSdkFixture("reference")

    @Test
    fun testResolveClassMemberSdkProperty() =
        gd.doResolveSymbolTest(
            "node.<caret>position",
            GdSdkPropertySymbol::class.java,
            "position",
        )

    @Test
    fun testResolveClassMemberSdkMethod() =
        gd.doResolveSymbolTest(
            "node.<caret>apply_scale",
            GdSdkMethodSymbol::class.java,
            "apply_scale",
        )

    @Test
    fun testResolveClassMemberSdkInferredTypeMethod() =
        gd.doResolveSymbolTest(
            "db.<caret>open_db()",
            GdSdkMethodSymbol::class.java,
            "open_db",
        )

    // RIDER-141201 guard/repro tests, sharing one fixture: a nested class referencing itself, or its
    // enclosing `class_name`d outer class, by bare name from within its own method body.
    @Test
    fun testResolveBareInnerClassSelfReference() =
        gd.doResolveSymbolTest(
            "<caret>Inner.factory_method()",
            GdPsiClassSymbol::class.java,
            "Inner",
        )

    @Test
    fun testResolveBareOuterClassReferenceFromInnerClass() =
        gd.doResolveSymbolTest(
            "<caret>TopLevel.factory_method",
            GdPsiClassSymbol::class.java,
            "TopLevel",
            chosenTestName = "resolveBareInnerClassSelfReference",
        )
}
