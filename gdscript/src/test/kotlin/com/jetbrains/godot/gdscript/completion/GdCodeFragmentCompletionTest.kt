package com.jetbrains.godot.gdscript.completion

import com.intellij.polySymbols.testFramework.renderLookupItems
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.gdscript.util.configureGdFragment
import com.jetbrains.godot.gdscript.util.disableSingleItemAutoInsert
import com.jetbrains.godot.gdscript.util.gdContextAt
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdCodeFragmentCompletionTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String =
        getBaseTestDataPath().resolve("testData/gdscript").pathString

    override fun setUp() {
        super.setUp()
        disableSingleItemAutoInsert(testRootDisposable)
    }

    private fun memberContext(): PsiElement = myFixture.gdContextAt(
        "context.gd",
        """
        |var health := 100
        |
        |func take_damage():
        |	<caret>pass
        """.trimMargin()
    )

    /** Sorted by name: order would otherwise encode lookup priority, which no test here is about. */
    private fun checkLookup(goldName: String = getTestName(true)) {
        myFixture.completeBasic()
        assertSameLinesWithFile(
            "$testDataPath/completion/codeFragment/$goldName.items.txt",
            myFixture.renderLookupItems(renderPriority = false, renderTypeText = false).sorted().joinToString("\n")
        )
    }

    @Test
    fun testFragmentOffersContextClassMembers() {
        myFixture.configureGdFragment("<caret>", memberContext())
        checkLookup()
    }

    @Test
    fun testFragmentOffersFrameLocalsDeclaredAboveTheContext() {
        val context = myFixture.gdContextAt(
            "context.gd",
            """
            |func _ready():
            |	var above := 1
            |	<caret>pass
            """.trimMargin()
        )

        myFixture.configureGdFragment("<caret>", context)
        checkLookup()
    }

    @Test
    @Ignore("RIDER-142331 GDScript: Completion in local scope should not offer symbols before they are declared")
    fun testFragmentDoesNotOfferLocalsDeclaredBelowTheContext() {
        val context = myFixture.gdContextAt(
            "context.gd",
            """
            |func _ready():
            |	var above := 1
            |	<caret>pass
            |	var below := 2
            """.trimMargin()
        )

        myFixture.configureGdFragment("<caret>", context)
        checkLookup()
    }

    @Test
    fun testFragmentDoesNotOfferLocalsOfAnotherFunction() {
        val context = myFixture.gdContextAt(
            "context.gd",
            """
            |func _ready():
            |	<caret>pass
            |
            |func _process(delta: float):
            |	var elsewhere := 1
            """.trimMargin()
        )

        myFixture.configureGdFragment("<caret>", context)
        checkLookup()
    }
}
