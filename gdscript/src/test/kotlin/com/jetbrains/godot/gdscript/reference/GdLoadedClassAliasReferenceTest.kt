package com.jetbrains.godot.gdscript.reference

import com.intellij.polySymbols.testFramework.multiResolveSymbolReference
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.polySymbols.psi.GdPsiLoadedClassAliasSymbol
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GdLoadedClassAliasReferenceTest {
    private val gd: GdSdkFixture by gdSdkFixture("reference")

    @Test
    fun testResolveLoadedClassAliasForScript() =
        gd.doResolveSymbolTest(
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
        gd.doConfiguredTest(dirName = dirName, configureFileName = "$dirName.${gd.defaultExtension}") {
            assertTrue(gd.fixture.multiResolveSymbolReference(signature).isEmpty(), "Expected '$signature' to have no resolved type-hint symbol")
        }
    }
}
