package com.jetbrains.godot.gdscript.redCode

import com.intellij.polySymbols.testFramework.checkGotoDeclaration
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Resolve and navigation"])
class GdEnumNavigationTest : BasePlatformTestCase() {
    fun testEnumMemberResolvesToEnumValue() {
        val code = """
          |enum _Anim {
          |  FLOOR,
          |  AIR,
          |}
          |
          |var anim : _Anim = _Anim.FLOOR
          |""".trimMargin()
        myFixture.configureByText("a.gd", code)
        myFixture.checkGotoDeclaration("_Anim.FL<caret>OOR", "<caret>FLOOR,")
    }
}
