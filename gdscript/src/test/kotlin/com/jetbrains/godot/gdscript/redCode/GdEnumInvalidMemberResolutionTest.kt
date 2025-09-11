package com.jetbrains.godot.gdscript.redCode

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.psi.GdRefIdRef

class GdEnumInvalidMemberResolutionTest : BasePlatformTestCase() {

    fun testInvalidEnumMemberDoesNotResolve() {
        val code = """
      |enum _Anim {
      |  FLOOR,
      |  AIR,
      |}
      |
      |var anim : _Anim = _Anim.WRONG
      |""".trimMargin()

        val psiFile = myFixture.configureByText("a.gd", code)

        val refs = PsiTreeUtil.collectElementsOfType(psiFile, GdRefIdRef::class.java)
        val target = refs.last { it.text == "WRONG" }

        val ref = target.references.firstOrNull()
        assertNotNull("Expected a reference object for enum member usage", ref)

        val resolved = ref!!.resolve()
        assertNull("Invalid enum member should not resolve", resolved)

        // Also ensure annotator marks this as an error
        val highlights: List<HighlightInfo> = myFixture.doHighlighting()
        val tr = target.textRange
        val hasError = highlights.any { hi ->
            hi.severity == HighlightSeverity.ERROR &&
            hi.description?.contains("Reference [WRONG] not found") == true &&
            hi.startOffset <= tr.startOffset && hi.endOffset >= tr.endOffset
        }
        assertTrue("Expected an annotator ERROR 'Reference [WRONG] not found' for WRONG", hasError)
    }
}
