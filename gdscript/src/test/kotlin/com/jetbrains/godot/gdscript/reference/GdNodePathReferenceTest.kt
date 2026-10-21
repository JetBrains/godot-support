package com.jetbrains.godot.gdscript.reference

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.testFramework.multiResolveSymbolReference
import com.intellij.polySymbols.testFramework.resolveSymbolReference
import com.intellij.polySymbols.utils.PolySymbolDelegate.Companion.unwrapAllDelegates
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.polySymbols.psi.GdPsiNodeSymbol
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GdNodePathReferenceTest {
    private val gd: GdSdkFixture by gdSdkFixture("completion")

    @Test
    fun testResolveDollarNodeToTscnNode() = doResolveTest("\$<caret>Plain") { node ->
        assertEquals("Plain", node.name)
    }

    @Test
    fun testResolveUnquotedUniqueNodeToTscnNode() = doResolveTest("%<caret>HealthBar") { node ->
        assertEquals("HealthBar", node.name)
    }

    @Test
    fun testResolveQuotedUniqueNodeToTscnNode() = doResolveTest("%\"<caret>Score Label\"") { node ->
        assertEquals("Score Label", node.name)
    }

    @Test
    fun testResolveInstancedNodeToTscnNode() = doResolveTest("\$<caret>Player") { node ->
        assertEquals("Player", node.name)
    }

    @Test
    fun testResolveQuotedRelativeParentPathToTscnNode() = gd.doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "deep_child.gd",
        fileContents = "extends Node2D\n\nfunc _ready():\n\tvar node = \$\"../../<caret>AnimationPlayer\"\n",
    ) {
        val node = resolveNodeSymbol("\$\"../../<caret>AnimationPlayer\"")
        assertEquals("AnimationPlayer", node.name)
    }

    @Test
    fun testResolveQuotedRelativeParentPathInOnreadyTypedDeclaration() = gd.doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "deep_child.gd",
        fileContents = "extends Node2D\n\n@onready var cube_animation: AnimationPlayer = \$\"../../<caret>AnimationPlayer\"\n",
    ) {
        val node = resolveNodeSymbol("\$\"../../<caret>AnimationPlayer\"")
        assertEquals("AnimationPlayer", node.name)
    }

    @Test
    fun testResolveRelativePathCrossingIntoInstancingScene() = gd.doConfiguredTest(
        dir = true,
        dirName = "crossScene",
        configureFileName = "inner.gd",
    ) {
        val node = resolveNodeSymbol("\$\"../<caret>Sibling\"")
        assertEquals("Sibling", node.name)
    }

    @Test
    fun testResolveRealVoxelDemoScriptCrossingIntoWorldScene() = gd.doConfiguredTest(
        dir = true,
        dirName = "voxelDemo",
        configureFileName = "player/player.gd",
    ) {
        val node = resolveNodeSymbol("\$\"../<caret>VoxelWorld\"")
        assertEquals("VoxelWorld", node.name)
    }

    @Test
    fun testResolveQuotedMultiSegmentDownPathToTscnNode() = gd.doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "deep_child.gd",
        fileContents = "extends Node2D\n\nfunc _ready():\n\tvar node = \$\"../../TopBar/ViewModeButtons/<caret>45Degree\"\n",
    ) {
        val node = resolveNodeSymbol("\$\"../../TopBar/ViewModeButtons/<caret>45Degree\"")
        assertEquals("45Degree", node.name)
    }

    @Test
    fun testResolveRealPauseDemoScript() = gd.doConfiguredTest(
        dir = true,
        dirName = "pauseDemo",
        configureFileName = "process_mode.gd",
    ) {
        val node = resolveNodeSymbol("\$\"../../<caret>AnimationPlayer\"")
        assertEquals("AnimationPlayer", node.name)
    }

    @Test
    fun testUnresolvedNodePathHasNoTarget() = gd.doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "unique_names.gd",
        fileContents = script("<caret>\$DoesNotExist"),
    ) {
        assertTrue(gd.fixture.multiResolveSymbolReference("<caret>\$DoesNotExist").isEmpty(), "an unknown node path must not resolve")
    }

    private fun script(expression: String): String =
        "extends Node2D\n\nfunc _ready():\n\tvar node = $expression\n"

    private fun doResolveTest(expressionWithCaret: String, check: (GdPsiNodeSymbol) -> Unit) =
        gd.doConfiguredTest(
            dir = true,
            dirName = "uniqueNames",
            configureFileName = "unique_names.gd",
            fileContents = script(expressionWithCaret),
        ) {
            check(resolveNodeSymbol(expressionWithCaret))
        }

    private fun resolveNodeSymbol(signature: String): GdPsiNodeSymbol {
        val referenced = gd.fixture.resolveSymbolReference(signature)
        val resolved = (referenced as? PolySymbol)?.unwrapAllDelegates() ?: referenced
        return assertInstanceOf(GdPsiNodeSymbol::class.java, resolved)
    }

}
