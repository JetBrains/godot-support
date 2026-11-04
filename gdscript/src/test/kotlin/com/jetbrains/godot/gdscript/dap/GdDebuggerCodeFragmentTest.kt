package com.jetbrains.godot.gdscript.dap

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.PsiElement
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.godot.gdscript.util.configureGdFragment
import com.jetbrains.godot.gdscript.util.gdContextAt
import com.jetbrains.godot.gdscript.util.gdFragment
import com.jetbrains.godot.gdscript.util.preserveGdAnnotatorSeverity
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Debug/GDScript/Evaluation"])
@TestApplication
class GdDebuggerCodeFragmentTest {

    private val gd: GdFixture by gdLightFixture("gdscript")

    @Test
    fun testProviderBuildsAFragmentThatKeptItsContextAndIsNotNamedGd() {
        val context = gd.fixture.gdContextAt(
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

        val fragment = gd.fixture.gdFragment("health < 10", context)

        assertSame(context, runReadActionBlocking { fragment.context }, "The debugger context element did not reach the fragment")
        assertFalse(fragment.name.endsWith(".gd"), "Fragment name is claimed by the Godot LSP: ${fragment.name}")
    }

    private fun playerContext(): PsiElement = gd.fixture.gdContextAt(
        "player.gd",
        """
        |var health := 100
        |
        |func _ready():
        |	<caret>pass
        """.trimMargin()
    )

    private fun fragmentProblems(text: String): List<HighlightInfo> {
        preserveGdAnnotatorSeverity(gd.testRootDisposable)
        gd.fixture.configureGdFragment(text, playerContext())
        return gd.fixture.doHighlighting().filter { it.severity >= HighlightSeverity.WARNING }
    }

    @Test
    fun testResolvableContextMemberIsNotHighlighted() {
        val problems = fragmentProblems("health")

        assertTrue(problems.map { it.description }.isEmpty())
    }

    /**
     *  Control: suppressing the annotator inside fragments would pass the
     *  `testResolvableContextMemberIsNotHighlighted` and destroy this one.
     */
    @Test
    fun testUnknownNameIsStillHighlighted() {
        val problems = fragmentProblems("nonsense")

        assertFalse(problems.isEmpty(), "an unresolvable name must still be flagged in the evaluate window")
    }
}
