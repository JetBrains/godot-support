package com.jetbrains.godot.gdscript.completion

import com.intellij.polySymbols.testFramework.renderLookupItems
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import com.jetbrains.godot.gdscript.util.configureGdFragment
import com.jetbrains.godot.gdscript.util.disableSingleItemAutoInsert
import com.jetbrains.godot.gdscript.util.gdContextAt
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/** No gold file here, please - `Node`'s lookup is ~480 entries and would churn on every SDK bump */
@RunWith(JUnit4::class)
class GdCodeFragmentCompletionSdkTest : GdTestCaseWithSdk("completion") {

    override fun setUp() {
        super.setUp()
        disableSingleItemAutoInsert(testRootDisposable)
    }

    // `self.` resolves through the class model, and without an SDK root that model yields nothing
    @Test
    fun testFragmentSelfDotOffersOwnAndInheritedMembers() {
        val context = myFixture.gdContextAt(
            "player.gd",
            """
            |extends Node
            |
            |var health := 100
            |
            |func _ready():
            |	<caret>pass
            """.trimMargin()
        )

        myFixture.configureGdFragment("self.<caret>", context)
        myFixture.completeBasic()

        val lookups = myFixture.renderLookupItems(renderPriority = false, renderTypeText = false)

        assertContainsElements(lookups, "health", "queue_free", "get_parent")
    }
}
