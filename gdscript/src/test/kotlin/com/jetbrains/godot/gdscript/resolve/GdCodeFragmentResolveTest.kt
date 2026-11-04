package com.jetbrains.godot.gdscript.resolve

import com.intellij.openapi.application.EDT
import com.intellij.testFramework.PlatformTestUtil.assertSameLinesWithFile
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdResolveFixture
import com.jetbrains.godot.gdscript.gdResolveFixture
import com.jetbrains.godot.gdscript.util.gdContextAt
import com.jetbrains.godot.gdscript.util.gdFragment
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Debug/GDScript/Evaluation"])
@TestApplication
class GdCodeFragmentResolveTest {
    private val gd: GdResolveFixture by gdResolveFixture("gdscript/resolve")

    private fun memberContext() = gd.fixture.gdContextAt(
        "context.gd",
        """
        |var health := 100
        |
        |func take_damage():
        |	<caret>pass
        """.trimMargin()
    )

    private fun doFragmentResolveTest(text: String) = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val fragment = gd.fixture.gdFragment(text, memberContext())
            assertSameLinesWithFile(
                "${gd.testDataPath}/codeFragment/${gd.getTestName(true)}.txt",
                gd.dumpResolvesWithInlineMarkers(fragment)
            )
        }
    }

    @Test
    fun testFragmentReferenceResolvesToTheContextClassMember() = doFragmentResolveTest("health < 10")

    /** Control: a resolver that answers everything would pass the `testFragmentReferenceToAnUnknownNameStaysUnresolved` and fail this one. */
    @Test
    fun testFragmentReferenceToAnUnknownNameStaysUnresolved() = doFragmentResolveTest("nonsense")
}
