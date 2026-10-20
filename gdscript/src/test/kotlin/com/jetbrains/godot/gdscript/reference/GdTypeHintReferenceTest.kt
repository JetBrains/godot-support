package com.jetbrains.godot.gdscript.reference

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.psi.GdPsiClassSymbol
import gdscript.polySymbols.psi.GdPsiEnumSymbol
import gdscript.polySymbols.sdk.GdSdkClassSymbol
import gdscript.polySymbols.sdk.GdSdkEnumSymbol
import org.junit.jupiter.api.Test

@TestApplication
class GdTypeHintReferenceTest : GdTestCaseWithSdk("reference") {

    @Test
    fun testResolveTypeHintSdkClass() =
        gd.doResolveSymbolTest(
            "var position_2d: <caret>Vector2",
            GdSdkClassSymbol::class.java,
            "Vector2",
        )

    @Test
    fun testResolveTypeHintEnum() =
        gd.doResolveSymbolTest(
            "var d: <caret>Direction",
            GdPsiEnumSymbol::class.java,
            "Direction",
        )

    @Test
    fun testResolveTypeHintSdkEnum() =
        gd.doResolveSymbolTest(
            "var p: Node.<caret>ProcessMode",
            GdSdkEnumSymbol::class.java,
            "ProcessMode",
        )

    @Test
    fun testResolveTypeHintBareInnerClass() =
        gd.doResolveSymbolTest(
            "var x: <caret>Inner",
            GdPsiClassSymbol::class.java,
            "Inner",
        )

    @Test
    fun testGotoDeclarationTypeHintSdkClass() =
        gd.doGotoDeclarationTest(
            declarationSignature = "class_name <caret>Vector2",
            fromSignature = "var position_2d: <caret>Vector2",
            expectedFileName = "Vector2.generated.gd",
            dirName = "resolveTypeHintSdkClass",
            configureFileName = "resolveTypeHintSdkClass.gd",
        )

    @Test
    fun testGotoDeclarationTypeHintSdkEnum() =
        gd.doGotoDeclarationTest(
            declarationSignature = "enum <caret>ProcessMode",
            fromSignature = "var p: Node.<caret>ProcessMode",
            expectedFileName = "Node.generated.gd",
            dirName = "resolveTypeHintSdkEnum",
            configureFileName = "resolveTypeHintSdkEnum.gd",
        )
}
