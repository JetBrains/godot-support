package com.jetbrains.godot.gdscript.redCode

import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.GdCodeInsightTestBase
import gdscript.psi.GdRefIdRef
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GdEnumNavigationTest : GdCodeInsightTestBase() {

    @Test
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
        assertNotNull(ref, "Expected a reference on enum member usage")

        val resolved = ref!!.resolve()
        assertNotNull(resolved, "Enum member reference should resolve to its declaration")
        assertEquals("FLOOR", resolved!!.text)
    }
}
