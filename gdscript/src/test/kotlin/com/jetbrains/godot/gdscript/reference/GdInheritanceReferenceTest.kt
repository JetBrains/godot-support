package com.jetbrains.godot.gdscript.reference

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.psi.GdPsiClassSymbol
import gdscript.polySymbols.psi.GdPsiResourceClassSymbol
import gdscript.polySymbols.sdk.GdSdkClassSymbol
import org.junit.jupiter.api.Test

@TestApplication
class GdInheritanceReferenceTest : GdTestCaseWithSdk("reference") {

    @Test
    fun testResolveUnnamedResourceInheritance() =
        gd.doResolveSymbolTest(
            "extends \"<caret>res://base.gd\"",
            GdPsiResourceClassSymbol::class.java,
            "res://base.gd"
        )

    @Test
    fun testResolveNamedResourceInheritance() =
        gd.doResolveSymbolTest(
            "extends \"<caret>res://base.gd\"",
            GdPsiClassSymbol::class.java,
            "Base"
        )

    @Test
    fun testResolveSdkInheritance() =
        gd.doResolveSymbolTest(
            "extends <caret>Node2D",
            GdSdkClassSymbol::class.java,
            "Node2D"
        )

    @Test
    fun testResolveLocalSdkInheritance() =
        gd.doResolveSymbolTest(
            "class Local extends <caret>Node2D",
            GdSdkClassSymbol::class.java,
            "Node2D"
        )

    @Test
    fun testResolveNestedClassInheritance() =
        gd.doResolveSymbolTest(
            "extends Outer.<caret>Inner",
            GdPsiClassSymbol::class.java,
            "Inner"
        )

    @Test
    fun testResolveBareSiblingClassInheritance() =
        gd.doResolveSymbolTest(
            "class Inner extends <caret>SuperInner",
            GdPsiClassSymbol::class.java,
            "SuperInner"
        )

    @Test
    fun testGotoDeclarationNamedResourceInheritance() =
        gd.doGotoDeclarationTest(
            declarationSignature = "class_name <caret>Base",
            fromSignature = "extends \"<caret>res://base.gd\"",
            expectedFileName = "base.gd",
            dirName = "resolveNamedResourceInheritance",
            configureFileName = "resolveNamedResourceInheritance.gd",
        )

    @Test
    fun testGotoDeclarationUnnamedResourceInheritance() =
        gd.doGotoDeclarationTest(
            declarationSignature = "<caret>var base_value = 1",
            fromSignature = "extends \"<caret>res://base.gd\"",
            expectedFileName = "base.gd",
            dirName = "resolveUnnamedResourceInheritance",
            configureFileName = "resolveUnnamedResourceInheritance.gd",
        )

    @Test
    fun testGotoDeclarationSdkInheritance() =
        gd.doGotoDeclarationTest(
            declarationSignature = "class_name <caret>Node2D",
            fromSignature = "extends <caret>Node2D",
            expectedFileName = "Node2D.generated.gd",
            dirName = "resolveSdkInheritance",
            configureFileName = "resolveSdkInheritance.gd",
        )
}
