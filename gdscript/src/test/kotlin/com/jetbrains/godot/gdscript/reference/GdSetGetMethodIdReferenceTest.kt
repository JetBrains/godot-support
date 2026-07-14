package com.jetbrains.godot.gdscript.reference

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.psi.GdPsiMethodSymbol
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdSetGetMethodIdReferenceTest : GdTestCaseWithSdk("reference") {

    @Test
    fun testResolveGetterMethod() =
        doResolveSymbolTest(
            "get = <caret>get_my_property",
            GdPsiMethodSymbol::class.java,
            "get_my_property",
            "resolveSetGetMethod",
        )

    @Test
    fun testResolveSetterMethod() =
        doResolveSymbolTest(
            "set = <caret>set_my_property",
            GdPsiMethodSymbol::class.java,
            "set_my_property",
            "resolveSetGetMethod",
        )

    @Test
    fun testGotoDeclarationGetterMethod() =
        doGotoDeclarationTest(
            declarationSignature = "func <caret>get_my_property() -> int:",
            fromSignature = "get = <caret>get_my_property",
            expectedFileName = "resolveSetGetMethod.gd",
            dirName = "resolveSetGetMethod",
            configureFileName = "resolveSetGetMethod.gd",
        )

    @Test
    fun testGotoDeclarationSetterMethod() =
        doGotoDeclarationTest(
            declarationSignature = "func <caret>set_my_property(value: int) -> void:",
            fromSignature = "set = <caret>set_my_property",
            expectedFileName = "resolveSetGetMethod.gd",
            dirName = "resolveSetGetMethod",
            configureFileName = "resolveSetGetMethod.gd",
        )
}
