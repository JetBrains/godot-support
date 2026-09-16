package com.jetbrains.godot.gdscript.reference

import com.intellij.polySymbols.testFramework.multiResolveSymbolReference
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.psi.GdPsiLoadedClassAliasSymbol
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdLoadedClassAliasReferenceTest : GdTestCaseWithSdk("reference") {

    @Test
    fun testResolveLoadedClassAliasForScript() =
        doResolveSymbolTest(
            "var typed_by_script: <caret>ScriptAlias",
            GdPsiLoadedClassAliasSymbol::class.java,
            "ScriptAlias",
            "resolveLoadedClassAlias",
        )

    @Test
    fun testLoadedSceneIsNotUsableAsTypeAlias() =
        doUnresolvedTypeHintTest("var typed_by_scene: <caret>SceneAlias")

    @Test
    fun testLoadedResourceIsNotUsableAsTypeAlias() =
        doUnresolvedTypeHintTest("var typed_by_resource: <caret>ResourceAlias")

    /**
     * A preloaded/loaded scene (`.tscn`) or resource (`.tres`) is a value, never a type - so its
     * name must not resolve in a type-hint position at all.
     */
    private fun doUnresolvedTypeHintTest(signature: String) {
        val dirName = "resolveLoadedClassAlias"
        doConfiguredTest(dirName = dirName, configureFileName = "$dirName.$defaultExtension") {
            assertTrue(
                "Expected '$signature' to have no resolved type-hint symbol",
                myFixture.multiResolveSymbolReference(signature).isEmpty(),
            )
        }
    }
}
