package com.jetbrains.godot.gdscript.redCode

import com.intellij.openapi.application.EDT
import com.intellij.polySymbols.testFramework.checkGotoDeclaration
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Resolve and navigation"])
@TestApplication
class GdEnumNavigationTest {
    private val gd: GdFixture by gdLightFixture("gdscript/redCode")

    @Test
    fun testEnumMemberResolvesToEnumValue() = timeoutRunBlocking {
        val code = """
          |enum _Anim {
          |  FLOOR,
          |  AIR,
          |}
          |
          |var anim : _Anim = _Anim.FLOOR
          |""".trimMargin()
        gd.fixture.configureByText("a.gd", code)
        withContext(Dispatchers.EDT) {
            gd.fixture.checkGotoDeclaration("_Anim.FL<caret>OOR", "<caret>FLOOR,")
        }
    }
}
