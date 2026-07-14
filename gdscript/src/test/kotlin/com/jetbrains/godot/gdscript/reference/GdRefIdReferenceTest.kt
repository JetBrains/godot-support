package com.jetbrains.godot.gdscript.reference

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.polySymbols.sdk.GdSdkPropertySymbol
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdRefIdReferenceTest : GdTestCaseWithSdk("reference") {

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
    fun testResolveClassMemberSdkInferredTypeMethod() =
        doResolveSymbolTest(
            "db.<caret>open_db()",
            GdSdkMethodSymbol::class.java,
            "open_db",
        )
}
