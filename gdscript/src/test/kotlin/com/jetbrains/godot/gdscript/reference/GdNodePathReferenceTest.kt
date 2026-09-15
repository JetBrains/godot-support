package com.jetbrains.godot.gdscript.reference

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import tscn.psi.TscnNodeHeader

@RunWith(JUnit4::class)
class GdNodePathReferenceTest : GdTestCaseWithSdk("completion") {

    @Test
    fun testResolveDollarNodeToTscnNode() = doResolveTest("\$Plain") { node ->
        assertEquals("Plain", node.name)
    }

    @Test
    fun testResolveUnquotedUniqueNodeToTscnNode() = doResolveTest("%HealthBar") { node ->
        assertEquals("HealthBar", node.name)
    }

    @Test
    fun testResolveQuotedUniqueNodeToTscnNode() = doResolveTest("%\"Score Label\"") { node ->
        assertEquals("Score Label", node.name)
    }

    @Test
    fun testResolveInstancedNodeToTscnNode() = doResolveTest("\$Player") { node ->
        assertEquals("Player", node.name)
    }

    @Test
    fun testResolveQuotedRelativeParentPathToTscnNode() = doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "deep_child.gd",
        fileContents = "extends Node2D\n\nfunc _ready():\n\tvar node = <caret>\$\"../../AnimationPlayer\"\n",
    ) {
        val reference = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("no reference found for \$\"../../AnimationPlayer\"", reference)
        val node = assertInstanceOf(reference!!.resolve(), TscnNodeHeader::class.java)
        assertEquals("AnimationPlayer", node.name)
    }

    @Test
    fun testResolveQuotedRelativeParentPathInOnreadyTypedDeclaration() = doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "deep_child.gd",
        fileContents = "extends Node2D\n\n@onready var cube_animation: AnimationPlayer = <caret>\$\"../../AnimationPlayer\"\n",
    ) {
        val reference = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("no reference found for \$\"../../AnimationPlayer\"", reference)
        val node = assertInstanceOf(reference!!.resolve(), TscnNodeHeader::class.java)
        assertEquals("AnimationPlayer", node.name)
    }

    @Test
    fun testResolveRelativePathCrossingIntoInstancingScene() = doConfiguredTest(
        dir = true,
        dirName = "crossScene",
        configureFileName = "inner.gd",
    ) {
        val reference = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("no reference found for \$\"../Sibling\"", reference)
        val node = assertInstanceOf(reference!!.resolve(), TscnNodeHeader::class.java)
        assertEquals("Sibling", node.name)
    }

    @Test
    fun testResolveRealVoxelDemoScriptCrossingIntoWorldScene() = doConfiguredTest(
        dir = true,
        dirName = "voxelDemo",
        configureFileName = "player/player.gd",
    ) {
        val reference = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("no reference found for \$\"../VoxelWorld\"", reference)
        val node = assertInstanceOf(reference!!.resolve(), TscnNodeHeader::class.java)
        assertEquals("VoxelWorld", node.name)
    }

    @Test
    fun testResolveQuotedMultiSegmentDownPathToTscnNode() = doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "deep_child.gd",
        fileContents = "extends Node2D\n\nfunc _ready():\n\tvar node = <caret>\$\"../../TopBar/ViewModeButtons/45Degree\"\n",
    ) {
        val reference = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("no reference found for \$\"../../TopBar/ViewModeButtons/45Degree\"", reference)
        val node = assertInstanceOf(reference!!.resolve(), TscnNodeHeader::class.java)
        assertEquals("45Degree", node.name)
    }

    @Test
    fun testResolveRealPauseDemoScript() = doConfiguredTest(
        dir = true,
        dirName = "pauseDemo",
        configureFileName = "process_mode.gd",
    ) {
        val reference = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("no reference found for \$\"../../AnimationPlayer\"", reference)
        val node = assertInstanceOf(reference!!.resolve(), TscnNodeHeader::class.java)
        assertEquals("AnimationPlayer", node.name)
    }

    @Test
    fun testUnresolvedNodePathHasNoTarget() = doConfiguredTest(
        dir = true,
        dirName = "uniqueNames",
        configureFileName = "unique_names.gd",
        fileContents = script("<caret>\$DoesNotExist"),
    ) {
        val reference = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("no reference found for \$DoesNotExist", reference)
        assertNull("an unknown node path must not resolve", reference!!.resolve())
    }

    private fun script(expression: String): String =
        "extends Node2D\n\nfunc _ready():\n\tvar node = $expression\n"

    private fun doResolveTest(expression: String, check: (TscnNodeHeader) -> Unit) =
        doConfiguredTest(
            dir = true,
            dirName = "uniqueNames",
            configureFileName = "unique_names.gd",
            fileContents = script("<caret>$expression"),
        ) {
            val reference = myFixture.file.findReferenceAt(myFixture.caretOffset)
            assertNotNull("no reference found for $expression", reference)
            val node = assertInstanceOf(reference!!.resolve(), TscnNodeHeader::class.java)
            check(node)
        }

}
