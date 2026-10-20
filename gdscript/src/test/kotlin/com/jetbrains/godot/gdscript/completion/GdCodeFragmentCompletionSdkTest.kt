package com.jetbrains.godot.gdscript.completion

import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.readAction
import com.intellij.polySymbols.testFramework.renderLookupItems
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import com.jetbrains.godot.gdscript.util.configureGdFragment
import com.jetbrains.godot.gdscript.util.disableSingleItemAutoInsert
import com.jetbrains.godot.gdscript.util.gdContextAt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/** No gold file here, please - `Node`'s lookup is ~480 entries and would churn on every SDK bump */
@TestApplication
class GdCodeFragmentCompletionSdkTest : GdTestCaseWithSdk("completion") {

    @BeforeEach
    fun disableAutoInsert() {
        disableSingleItemAutoInsert(gd.testRootDisposable)
    }

    // `self.` resolves through the class model, and without an SDK root that model yields nothing
    @Test
    fun testFragmentSelfDotOffersOwnAndInheritedMembers(): Unit = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
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
        }
        myFixture.completeBasic()

        val lookups = readAction { myFixture.renderLookupItems(renderPriority = false, renderTypeText = false) }

        assertThat(lookups).contains("health", "queue_free", "get_parent")
    }
}
