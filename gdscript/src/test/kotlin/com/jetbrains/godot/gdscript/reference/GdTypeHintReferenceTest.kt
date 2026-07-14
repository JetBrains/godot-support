package com.jetbrains.godot.gdscript.reference

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.psi.GdPsiEnumSymbol
import gdscript.polySymbols.sdk.GdSdkClassSymbol
import gdscript.polySymbols.sdk.GdSdkEnumSymbol
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdTypeHintReferenceTest : GdTestCaseWithSdk("reference") {

    @Test
    fun testResolveTypeHintSdkClass() =
        doResolveSymbolTest(
            "var position_2d: <caret>Vector2",
            GdSdkClassSymbol::class.java,
            "Vector2",
        )

    @Test
    fun testResolveTypeHintEnum() =
        doResolveSymbolTest(
            "var d: <caret>Direction",
            GdPsiEnumSymbol::class.java,
            "Direction",
        )

    @Test
    fun testResolveTypeHintSdkEnum() =
        doResolveSymbolTest(
            "var p: Node.<caret>ProcessMode",
            GdSdkEnumSymbol::class.java,
            "ProcessMode",
        )

    @Test
    fun testGotoDeclarationTypeHintSdkClass() =
        doGotoDeclarationTest(
            declarationSignature = "class_name <caret>Vector2",
            fromSignature = "var position_2d: <caret>Vector2",
            expectedFileName = "Vector2.gd",
            dirName = "resolveTypeHintSdkClass",
            configureFileName = "resolveTypeHintSdkClass.gd",
        )

    @Test
    fun testGotoDeclarationTypeHintSdkEnum() =
        doGotoDeclarationTest(
            declarationSignature = "enum <caret>ProcessMode",
            fromSignature = "var p: Node.<caret>ProcessMode",
            expectedFileName = "Node.gd",
            dirName = "resolveTypeHintSdkEnum",
            configureFileName = "resolveTypeHintSdkEnum.gd",
        )
}
