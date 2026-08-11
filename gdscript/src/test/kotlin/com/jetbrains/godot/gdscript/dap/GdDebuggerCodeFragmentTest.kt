package com.jetbrains.godot.gdscript.dap

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.gdscript.util.configureGdFragment
import com.jetbrains.godot.gdscript.util.gdContextAt
import com.jetbrains.godot.gdscript.util.gdFragment
import com.jetbrains.godot.gdscript.util.preserveGdAnnotatorSeverity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdDebuggerCodeFragmentTest : BasePlatformTestCase() {

    @Test
    fun testProviderBuildsAFragmentThatKeptItsContextAndIsNotNamedGd() {
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

        val fragment = myFixture.gdFragment("health < 10", context)

        assertSame("The debugger context element did not reach the fragment", context, fragment.context)
        assertFalse("Fragment name is claimed by the Godot LSP: ${fragment.name}", fragment.name.endsWith(".gd"))
    }

    private fun playerContext(): PsiElement = myFixture.gdContextAt(
        "player.gd",
        """
        |var health := 100
        |
        |func _ready():
        |	<caret>pass
        """.trimMargin()
    )

    private fun fragmentProblems(text: String): List<HighlightInfo> {
        preserveGdAnnotatorSeverity(testRootDisposable)
        myFixture.configureGdFragment(text, playerContext())
        return myFixture.doHighlighting().filter { it.severity >= HighlightSeverity.WARNING }
    }

    @Test
    fun testResolvableContextMemberIsNotHighlighted() {
        val problems = fragmentProblems("health")

        assertEmpty(problems.map { it.description })
    }

    /**
     *  Control: suppressing the annotator inside fragments would pass the
     *  `testResolvableContextMemberIsNotHighlighted` and destroy this one.
     */
    @Test
    fun testUnknownNameIsStillHighlighted() {
        val problems = fragmentProblems("nonsense")

        assertFalse("an unresolvable name must still be flagged in the evaluate window", problems.isEmpty())
    }
}
