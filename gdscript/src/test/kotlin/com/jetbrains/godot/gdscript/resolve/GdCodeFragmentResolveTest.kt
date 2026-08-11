package com.jetbrains.godot.gdscript.resolve

import com.jetbrains.godot.gdscript.util.gdContextAt
import com.jetbrains.godot.gdscript.util.gdFragment
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdCodeFragmentResolveTest : ResolveTestBase() {

    private fun memberContext() = myFixture.gdContextAt(
        "context.gd",
        """
        |var health := 100
        |
        |func take_damage():
        |	<caret>pass
        """.trimMargin()
    )

    private fun doFragmentResolveTest(text: String) {
        val fragment = myFixture.gdFragment(text, memberContext())
        assertSameLinesWithFile(
            "$testDataPath/codeFragment/${getTestName(true)}.txt",
            dumpResolvesWithInlineMarkers(fragment)
        )
    }

    @Test
    fun testFragmentReferenceResolvesToTheContextClassMember() = doFragmentResolveTest("health < 10")

    /** Control: a resolver that answers everything would pass the `testFragmentReferenceToAnUnknownNameStaysUnresolved` and fail this one. */
    @Test
    fun testFragmentReferenceToAnUnknownNameStaysUnresolved() = doFragmentResolveTest("nonsense")
}
