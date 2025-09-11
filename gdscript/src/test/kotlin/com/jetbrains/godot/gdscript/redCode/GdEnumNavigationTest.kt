package com.jetbrains.godot.gdscript.redCode

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.psi.GdRefIdRef

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

    val psiFile = myFixture.configureByText("a.gd", code)

    val refs = PsiTreeUtil.collectElementsOfType(psiFile, GdRefIdRef::class.java)
    val target = refs.last { it.text == "FLOOR" }

    val ref = target.references.firstOrNull()
    assertNotNull("Expected a reference on enum member usage", ref)

    val resolved = ref!!.resolve()
    assertNotNull("Enum member reference should resolve to its declaration", resolved)
    assertEquals("FLOOR", resolved!!.text)
  }
}
