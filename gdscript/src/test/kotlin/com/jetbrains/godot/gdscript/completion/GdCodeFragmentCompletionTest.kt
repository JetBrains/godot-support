package com.jetbrains.godot.gdscript.completion

import com.intellij.polySymbols.testFramework.renderLookupItems
import com.intellij.psi.PsiElement
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.godot.gdscript.util.configureGdFragment
import com.jetbrains.godot.gdscript.util.disableSingleItemAutoInsert
import com.jetbrains.godot.gdscript.util.gdContextAt
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Debug/GDScript/Completion in Evaluate window"])
@TestApplication
class GdCodeFragmentCompletionTest {

    private val gd: GdFixture by gdLightFixture("gdscript/completion")

    @BeforeEach
    fun disableAutoInsert() {
        disableSingleItemAutoInsert(gd.testRootDisposable)
    }

    private fun memberContext(): PsiElement = gd.fixture.gdContextAt(
        "context.gd",
        """
        |var health := 100
        |
        |func take_damage():
        |	<caret>pass
        """.trimMargin()
    )

    /** Sorted by name: order would otherwise encode lookup priority, which no test here is about. */
    private fun checkLookup(goldName: String = gd.testName) {
        gd.fixture.completeBasic()
        PlatformTestUtil.assertSameLinesWithFile(
            "${gd.testDataPath}/codeFragment/$goldName.items.txt",
            gd.fixture.renderLookupItems(renderPriority = false, renderTypeText = false).sorted().joinToString("\n")
        )
    }

    @Test
    fun testFragmentOffersContextClassMembers() {
        gd.fixture.configureGdFragment("<caret>", memberContext())
        checkLookup()
    }

    @Test
    fun testFragmentOffersFrameLocalsDeclaredAboveTheContext() {
        val context = gd.fixture.gdContextAt(
            "context.gd",
            """
            |func _ready():
            |	var above := 1
            |	<caret>pass
            """.trimMargin()
        )

        gd.fixture.configureGdFragment("<caret>", context)
        checkLookup()
    }

    @Test
    @Disabled("RIDER-142331 GDScript: Completion in local scope should not offer symbols before they are declared")
    fun testFragmentDoesNotOfferLocalsDeclaredBelowTheContext() {
        val context = gd.fixture.gdContextAt(
            "context.gd",
            """
            |func _ready():
            |	var above := 1
            |	<caret>pass
            |	var below := 2
            """.trimMargin()
        )

        gd.fixture.configureGdFragment("<caret>", context)
        checkLookup()
    }

    @Test
    fun testFragmentDoesNotOfferLocalsOfAnotherFunction() {
        val context = gd.fixture.gdContextAt(
            "context.gd",
            """
            |func _ready():
            |	<caret>pass
            |
            |func _process(delta: float):
            |	var elsewhere := 1
            """.trimMargin()
        )

        gd.fixture.configureGdFragment("<caret>", context)
        checkLookup()
    }
}
