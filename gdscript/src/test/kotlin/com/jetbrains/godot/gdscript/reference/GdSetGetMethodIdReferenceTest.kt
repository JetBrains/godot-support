package com.jetbrains.godot.gdscript.reference

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.polySymbols.psi.GdPsiMethodSymbol
import org.junit.jupiter.api.Test

@TestApplication
class GdSetGetMethodIdReferenceTest {
    private val gd: GdSdkFixture by gdSdkFixture("reference")

    @Test
    fun testResolveGetterMethod() =
        gd.doResolveSymbolTest(
            "get = <caret>get_my_property",
            GdPsiMethodSymbol::class.java,
            "get_my_property",
            "resolveSetGetMethod",
        )

    @Test
    fun testResolveSetterMethod() =
        gd.doResolveSymbolTest(
            "set = <caret>set_my_property",
            GdPsiMethodSymbol::class.java,
            "set_my_property",
            "resolveSetGetMethod",
        )

    @Test
    fun testGotoDeclarationGetterMethod() =
        gd.doGotoDeclarationTest(
            declarationSignature = "func <caret>get_my_property() -> int:",
            fromSignature = "get = <caret>get_my_property",
            expectedFileName = "resolveSetGetMethod.gd",
            dirName = "resolveSetGetMethod",
            configureFileName = "resolveSetGetMethod.gd",
        )

    @Test
    fun testGotoDeclarationSetterMethod() =
        gd.doGotoDeclarationTest(
            declarationSignature = "func <caret>set_my_property(value: int) -> void:",
            fromSignature = "set = <caret>set_my_property",
            expectedFileName = "resolveSetGetMethod.gd",
            dirName = "resolveSetGetMethod",
            configureFileName = "resolveSetGetMethod.gd",
        )
}
