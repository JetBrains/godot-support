package com.jetbrains.godot.gdscript.polySymbols.reference

import com.jetbrains.godot.gdscript.polySymbols.GdPolySymbolsTestCaseWithSdk
import gdscript.polySymbols.psi.GdPsiClassSymbol
import gdscript.polySymbols.psi.GdPsiResourceClassSymbol
import gdscript.polySymbols.sdk.GdSdkClassSymbol
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdInheritancePolySymbolReferenceTest : GdPolySymbolsTestCaseWithSdk("reference") {

    @Test
    fun testResolveUnnamedResourceInheritance() =
        doResolveSymbolTest(
            "extends \"<caret>res://base.gd\"",
            GdPsiResourceClassSymbol::class.java,
            "res://base.gd"
        )

    @Test
    fun testResolveNamedResourceInheritance() =
        doResolveSymbolTest(
            "extends \"<caret>res://base.gd\"",
            GdPsiClassSymbol::class.java,
            "Base"
        )

    @Test
    fun testResolveSdkInheritance() =
        doResolveSymbolTest(
            "extends <caret>Node2D",
            GdSdkClassSymbol::class.java,
            "Node2D"
        )

    @Test
    fun testResolveLocalSdkInheritance() =
        doResolveSymbolTest(
            "class Local extends <caret>Node2D",
            GdSdkClassSymbol::class.java,
            "Node2D"
        )

    @Test
    fun testGotoDeclarationNamedResourceInheritance() =
        doGotoDeclarationTest(
            declarationSignature = "class_name <caret>Base",
            fromSignature = "extends \"<caret>res://base.gd\"",
            expectedFileName = "base.gd",
            dirName = "resolveNamedResourceInheritance",
            configureFileName = "resolveNamedResourceInheritance.gd",
        )

    @Test
    fun testGotoDeclarationUnnamedResourceInheritance() =
        doGotoDeclarationTest(
            declarationSignature = "<caret>var base_value = 1",
            fromSignature = "extends \"<caret>res://base.gd\"",
            expectedFileName = "base.gd",
            dirName = "resolveUnnamedResourceInheritance",
            configureFileName = "resolveUnnamedResourceInheritance.gd",
        )

    @Test
    fun testGotoDeclarationSdkInheritance() =
        doGotoDeclarationTest(
            declarationSignature = "class_name <caret>Node2D",
            fromSignature = "extends <caret>Node2D",
            expectedFileName = "Node2D.gd",
            dirName = "resolveSdkInheritance",
            configureFileName = "resolveSdkInheritance.gd",
        )
}
