package com.jetbrains.godot.gdscript.polySymbols.reference

import com.jetbrains.godot.gdscript.polySymbols.GdPolySymbolsTestCaseWithSdk
import gdscript.polySymbols.psi.GdPsiMethodSymbol
import gdscript.polySymbols.psi.GdPsiPropertySymbol
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.polySymbols.sdk.GdSdkPropertySymbol
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdClassMemberPolySymbolReferenceTest : GdPolySymbolsTestCaseWithSdk("reference") {

    @Test
    fun testResolveClassMemberSdkProperty() =
        doResolveSymbolTest(
            "node.<caret>position",
            GdSdkPropertySymbol::class.java,
            "position",
        )

    @Test
    fun testResolveClassMemberSdkMethod() =
        doResolveSymbolTest(
            "node.<caret>apply_scale",
            GdSdkMethodSymbol::class.java,
            "apply_scale",
        )

    @Test
    fun testResolveClassMemberPsiProperty() =
        doResolveSymbolTest(
            "obj.<caret>my_prop",
            GdPsiPropertySymbol::class.java,
            "my_prop",
        )

    @Test
    fun testResolveClassMemberPsiMethod() =
        doResolveSymbolTest(
            "obj.<caret>my_method",
            GdPsiMethodSymbol::class.java,
            "my_method",
        )
}
