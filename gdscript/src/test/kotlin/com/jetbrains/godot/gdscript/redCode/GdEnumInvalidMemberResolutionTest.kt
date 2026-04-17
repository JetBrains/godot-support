package com.jetbrains.godot.gdscript.redCode

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.TestModeFlags
import com.jetbrains.godot.GdCodeInsightTestBase
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import gdscript.psi.GdRefIdRef
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GdEnumInvalidMemberResolutionTest : GdCodeInsightTestBase() {

    @BeforeEach
    fun setUpAnnotatorMode() {
        TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, testRootDisposable)
    }

    @Test
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
        assertNotNull(ref, "Expected a reference object for enum member usage")

        val resolved = ref!!.resolve()
        assertNull(resolved, "Invalid enum member should not resolve")

        // Also ensure annotator marks this as an error
        val highlights: List<HighlightInfo> = myFixture.doHighlighting()
        val tr = target.textRange
        val hasError = highlights.any { hi ->
            hi.severity == HighlightSeverity.ERROR &&
            hi.description?.contains("Reference [WRONG] not found") == true &&
            hi.startOffset <= tr.startOffset && hi.endOffset >= tr.endOffset
        }
        assertTrue(hasError, "Expected an annotator ERROR 'Reference [WRONG] not found' for WRONG")
    }
}
