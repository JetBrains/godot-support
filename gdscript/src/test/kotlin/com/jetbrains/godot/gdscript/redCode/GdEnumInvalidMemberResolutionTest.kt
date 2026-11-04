package com.jetbrains.godot.gdscript.redCode

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.polySymbols.testFramework.psiSymbolReferences
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.TestModeFlags
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import gdscript.psi.GdRefIdRef
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Red code"])
@TestApplication
class GdEnumInvalidMemberResolutionTest {

    private val gd: GdFixture by gdLightFixture("gdscript/highlighting")

    @BeforeEach
    fun preserveAnnotatorSeverity() {
        TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, gd.testRootDisposable)
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

        val psiFile = gd.fixture.configureByText("a.gd", code)

        val tr = runReadActionBlocking {
            val refs = PsiTreeUtil.collectElementsOfType(psiFile, GdRefIdRef::class.java)
            val target = refs.last { it.text == "WRONG" }

            val ref = target.psiSymbolReferences().firstOrNull()
            assertNotNull(ref, "Expected a reference object for enum member usage")

            val resolved = ref!!.resolveReference().firstOrNull()
            assertNull(resolved, "Invalid enum member should not resolve")

            target.textRange
        }

        // Also ensure annotator marks this as an error
        val highlights: List<HighlightInfo> = gd.fixture.doHighlighting()
        val hasError = highlights.any { hi ->
            hi.severity == HighlightSeverity.ERROR &&
            hi.description?.contains("Reference [WRONG] not found") == true &&
            hi.startOffset <= tr.startOffset && hi.endOffset >= tr.endOffset
        }
        assertTrue(hasError, "Expected an annotator ERROR 'Reference [WRONG] not found' for WRONG")
    }
}
