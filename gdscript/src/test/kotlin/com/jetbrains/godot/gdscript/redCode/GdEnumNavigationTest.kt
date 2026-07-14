package com.jetbrains.godot.gdscript.redCode

import com.intellij.polySymbols.testFramework.checkGotoDeclaration
import com.intellij.testFramework.fixtures.BasePlatformTestCase

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
